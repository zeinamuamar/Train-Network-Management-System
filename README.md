# Train Network Management System

This repository contains a Train Network Management System (placeholder).

Contents added as an initial commit:

- `src/Main.java` — minimal Java entrypoint

Quick start (compile & run):

```powershell
cd "Train-Network-Management-System"
javac -d out src/Main.java
java -cp out Main
```

How to push local files (if you own the repo):

```powershell
cd "C:\Users\CP24\OneDrive\Train Network Management System\Train-Network-Management-System"
git add .
git commit -m "Initial commit"
git push origin main
```

If `git push` prompts for credentials, use your GitHub credentials or a Personal Access Token (recommended).

## Auto-build & run watcher

To automatically compile and run the app whenever you save Java files in `src/`, use the provided PowerShell watcher.

Start the watcher (keeps running and restarts the app on changes):

```powershell
cd "C:\Users\CP24\OneDrive\Train Network Management System\Train-Network-Management-System"
powershell -NoProfile -ExecutionPolicy Bypass -File .\auto-build-run.ps1
```

Stop the watcher by closing the terminal running it or by killing the process in Task Manager.

You can also run the simple script `run.bat` to compile and run once:

```powershell
cd "C:\Users\CP24\OneDrive\Train Network Management System\Train-Network-Management-System"
.\run.bat
```

If you want the watcher to start automatically when opening the workspace in VS Code, tell me and I'll add a recommended workspace task.
