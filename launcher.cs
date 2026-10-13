using System;
using System.Diagnostics;
using System.IO;
using System.Windows.Forms;

namespace SmartLostAndFoundLauncher
{
    static class Program
    {
        [STAThread]
        static void Main(string[] args)
        {
            try
            {
                string appDir = AppDomain.CurrentDomain.BaseDirectory;
                string jarPath = Path.Combine(appDir, "SmartLostAndFound.jar");

                if (!File.Exists(jarPath))
                {
                    MessageBox.Show(
                        "Cannot find 'SmartLostAndFound.jar' in:\n" + appDir + "\n\nPlease ensure the JAR file is in the same folder as this executable.",
                        "Smart Lost & Found - Missing File",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                    return;
                }

                string javaExe = FindJavaExecutable(appDir);

                if (string.IsNullOrEmpty(javaExe) || !File.Exists(javaExe))
                {
                    MessageBox.Show(
                        "Java Runtime Environment (Java 17+) was not detected on your system.\n\n" +
                        "Checked paths include:\n" +
                        "- " + Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.UserProfile), @".jdk17\bin\javaw.exe") + "\n" +
                        "- %JAVA_HOME%\\bin\\javaw.exe\n" +
                        "- System PATH\n\n" +
                        "Please ensure Java 17 is installed.",
                        "Java Required",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Warning);
                    return;
                }

                ProcessStartInfo psi = new ProcessStartInfo();
                psi.FileName = javaExe;
                psi.Arguments = "-jar \"" + jarPath + "\"";
                psi.WorkingDirectory = appDir;
                psi.UseShellExecute = true;

                Process.Start(psi);
            }
            catch (Exception ex)
            {
                MessageBox.Show(
                    "Error launching Smart Lost & Found:\n" + ex.Message,
                    "Launch Error",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
        }

        private static string FindJavaExecutable(string appDir)
        {
            // 1. Check relative bundled runtimes
            string[] localCandidates = new string[]
            {
                Path.Combine(appDir, @"jre\bin\javaw.exe"),
                Path.Combine(appDir, @"jdk\bin\javaw.exe"),
                Path.Combine(appDir, @"tools\jdk-17\bin\javaw.exe")
            };

            foreach (string candidate in localCandidates)
            {
                if (File.Exists(candidate)) return candidate;
            }

            // 2. Check user's installed portable JDK 17
            string userProfile = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile);
            string userJdkW = Path.Combine(userProfile, @".jdk17\bin\javaw.exe");
            if (File.Exists(userJdkW)) return userJdkW;

            string userJdk = Path.Combine(userProfile, @".jdk17\bin\java.exe");
            if (File.Exists(userJdk)) return userJdk;

            // 3. Check JAVA_HOME environment variable
            string javaHome = Environment.GetEnvironmentVariable("JAVA_HOME");
            if (!string.IsNullOrEmpty(javaHome))
            {
                string homeJavaw = Path.Combine(javaHome, @"bin\javaw.exe");
                if (File.Exists(homeJavaw)) return homeJavaw;
                string homeJava = Path.Combine(javaHome, @"bin\java.exe");
                if (File.Exists(homeJava)) return homeJava;
            }

            // 4. Check system Program Files
            string[] systemPaths = new string[]
            {
                @"C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot\bin\javaw.exe",
                @"C:\Program Files\Java\jdk-17\bin\javaw.exe",
                @"C:\Program Files\Java\jdk-21\bin\javaw.exe"
            };

            foreach (string sys in systemPaths)
            {
                if (File.Exists(sys)) return sys;
            }

            // 5. Search PATH
            string pathEnv = Environment.GetEnvironmentVariable("PATH");
            if (!string.IsNullOrEmpty(pathEnv))
            {
                string[] paths = pathEnv.Split(';');
                foreach (string p in paths)
                {
                    try
                    {
                        string javaw = Path.Combine(p.Trim(), "javaw.exe");
                        if (File.Exists(javaw)) return javaw;
                        string java = Path.Combine(p.Trim(), "java.exe");
                        if (File.Exists(java)) return java;
                    }
                    catch { }
                }
            }

            return null;
        }
    }
}
