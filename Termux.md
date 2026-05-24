# Running JNIC on Termux (Android)

## Prerequisites

Install the required packages in Termux:

```bash
pkg update
pkg install clang openjdk-21
```

## Usage

1. Copy your `Jnic-3.6.1.jar` and your target `.jar` to a folder in Termux.

2. Create a `config.xml` by running the tool once with no config:
```bash
java -jar Jnic-3.6.1.jar input.jar output/
```

3. Edit `config.xml` — enable the Android target and disable desktop targets:
```xml
<jnic>
    <targets>
        <!-- Only Android target on Termux -->
        <target>ANDROID_AARCH64</target>
    </targets>
    <options>
        <stringObf>false</stringObf>
        <flowObf>false</flowObf>
    </options>
    <include>
        <match className="**" />
    </include>
    <exclude>
    </exclude>
</jnic>
```

4. Run the tool:
```bash
java -jar Jnic-3.6.1.jar input.jar output/ -c config.xml
```

The tool will:
- Detect Termux automatically
- Skip the Zig download
- Use your system `clang` to compile the native library
- Output `arm64-android.so` linked against Android's Bionic libc

## Notes

- On Termux you can only compile for **your device's own ABI** (aarch64 for most phones).
  Cross-compilation to Windows/macOS/Linux requires a different machine.
- The generated `.so` uses API level 21 (Android 5.0+).
- If clang is not found, the tool will error. Run `pkg install clang` to fix it.
- 
