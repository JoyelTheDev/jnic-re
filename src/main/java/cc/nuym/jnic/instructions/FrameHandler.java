package cc.nuym.jnic.instructions;

import cc.nuym.jnic.utils.MethodContext;
import cc.nuym.jnic.MethodProcessor;
import cc.nuym.jnic.utils.Util;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;

import java.util.Arrays;
import java.util.function.Consumer;

public class FrameHandler implements InstructionTypeHandler<FrameNode> {
    @Override
    public void accept(MethodContext context, FrameNode node) {
        Consumer<Object> appendLocal = local -> {
            if (local instanceof String || local instanceof LabelNode) {
                context.locals.add(MethodProcessor.TYPE_TO_STACK[Type.OBJECT]);
            } else {
                context.locals.add(MethodProcessor.STACK_TO_STACK[(int) local]);
            }
        };

        Consumer<Object> appendStack = stack -> {
            if (stack instanceof String || stack instanceof LabelNode) {
                context.stack.add(MethodProcessor.TYPE_TO_STACK[Type.OBJECT]);
            } else {
                context.stack.add(MethodProcessor.STACK_TO_STACK[(int) stack]);
            }
        };

        switch (node.type) {
            case Opcodes.F_APPEND -> {
                node.local.forEach(appendLocal);
                context.stack.clear();
            }
            case Opcodes.F_CHOP -> {
                node.local.forEach(item -> context.locals.remove(context.locals.size() - 1));
                context.stack.clear();
            }
            case Opcodes.F_NEW, Opcodes.F_FULL -> {
                context.locals.clear();
                context.stack.clear();
                node.local.forEach(appendLocal);
                node.stack.forEach(appendStack);
            }
            case Opcodes.F_SAME -> context.stack.clear();
            case Opcodes.F_SAME1 -> {
                context.stack.clear();
                appendStack.accept(node.stack.get(0));
            }
        }
    }

    @Override
    public String insnToString(MethodContext context, FrameNode node) {
        return String.format("FRAME %s L: %s S: %s", Util.getOpcodesString(node.type, "F_"),
                node.local == null ? "null" : Arrays.toString(node.local.toArray(new Object[0])),
                node.stack == null ? "null" : Arrays.toString(node.stack.toArray(new Object[0])));
    }

    @Override
    public int getNewStackPointer(FrameNode node, int currentStackPointer) {
        return switch (node.type) {
            case Opcodes.F_APPEND, Opcodes.F_SAME, Opcodes.F_CHOP -> 0;
            case Opcodes.F_NEW, Opcodes.F_FULL -> node.stack.stream()
                    .mapToInt(argument -> Math.max(1, argument instanceof Integer
                            ? MethodProcessor.STACK_TO_STACK[(int) argument]
                            : MethodProcessor.TYPE_TO_STACK[Type.OBJECT]))
                    .sum();
            case Opcodes.F_SAME1 -> node.stack.stream().limit(1)
                    .mapToInt(argument -> Math.max(1, argument instanceof Integer
                            ? MethodProcessor.STACK_TO_STACK[(int) argument]
                            : MethodProcessor.TYPE_TO_STACK[Type.OBJECT]))
                    .sum();
            default -> throw new RuntimeException();
        };
    }
}
