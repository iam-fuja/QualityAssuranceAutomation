package resources;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Professional Report Delivery Utility
 * Handles email delivery, cloud upload, and report bundling
 * Works in both local and CI/CD environments
 *
 * @author MSDAT QA Team
 * @version 2.0
 */
public class ReportDeliveryUtil {

    // Configuration constants
    private static final String DEFAULT_SMTP_HOST = "smtp.gmail.com";
    private static final String DEFAULT_SMTP_PORT = "587";
    private static final String DEFAULT_FROM_NAME = "MSDAT Automation Suite";

    // Email templates
    private static final String EMAIL_STYLE = """
        <style>
            body { font-family: 'Segoe UI', Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }
            .container { max-width: 800px; margin: 0 auto; background-color: white; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
            .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }
            .content { padding: 30px; }
            .status-success { color: #4CAF50; font-weight: bold; }
            .status-failure { color: #f44336; font-weight: bold; }
            .results-table { width: 100%; border-collapse: collapse; margin: 20px 0; }
            .results-table th, .results-table td { padding: 12px; text-align: center; border: 1px solid #ddd; }
            .results-table th { background-color: #f8f9fa; font-weight: bold; }
            .passed { color: #4CAF50; font-weight: bold; }
            .failed { color: #f44336; font-weight: bold; }
            .skipped { color: #ff9800; font-weight: bold; }
            .instructions { background-color: #e3f2fd; padding: 20px; border-radius: 5px; border-left: 4px solid #2196F3; margin: 20px 0; }
            .footer { background-color: #f8f9fa; padding: 20px; text-align: center; font-size: 12px; color: #666; }
            .attachment-list { background-color: #f9f9f9; padding: 15px; border-radius: 5px; margin: 15px 0; }
            .failed-tests { background-color: #ffebee; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #f44336; }
        </style>
        """;

    /**
     * MAIN METHOD: For use from production code (base class)
     * Sends simple email notification with ZIP attachment
     *
     * @param zipFile The ZIP file containing reports and screenshots
     */
    public static void emailZipReport(File zipFile) {
        emailTestReport(zipFile, null);
    }

    /**
     * ENHANCED METHOD: For use from test code with full context
     * Sends professional email with detailed test summary
     *
     * @param zipFile The ZIP file containing reports and screenshots
     * @param context TestNG context containing test execution details
     */
    public static void emailTestReport(File zipFile, ITestContext context) {
        // Get configuration from environment variables (CI-friendly)
        String username = getConfigValue("EMAIL_USERNAME", "your-email@gmail.com");
        String password = getConfigValue("EMAIL_PASSWORD", "your-app-password");
        String recipients = getConfigValue("EMAIL_RECIPIENTS", "qa-team@company.com");
        String smtpHost = getConfigValue("SMTP_HOST", DEFAULT_SMTP_HOST);
        String smtpPort = getConfigValue("SMTP_PORT", DEFAULT_SMTP_PORT);

        // Skip email if credentials not configured (graceful degradation)
        if (password.equals("your-app-password") || password.isEmpty() ||
                username.equals("your-email@gmail.com")) {
            System.out.println("⚠️ Email credentials not configured. Skipping email delivery.");
            System.out.println("💡 Set EMAIL_USERNAME, EMAIL_PASSWORD, and EMAIL_RECIPIENTS environment variables to enable email.");
            return;
        }

        // Validate ZIP file
        if (zipFile == null || !zipFile.exists()) {
            System.err.println("❌ ZIP file not found or invalid: " + (zipFile != null ? zipFile.getAbsolutePath() : "null"));
            return;
        }

        try {
            // Configure SMTP properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);
            props.put("mail.smtp.ssl.trust", smtpHost);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            // ADD THESE TIMEOUT PROPERTIES:
            props.put("mail.smtp.connectiontimeout", "60000");   // 60 seconds
            props.put("mail.smtp.timeout", "120000");            // 2 minutes
            props.put("mail.smtp.writetimeout", "120000");       // 2 minutes
            props.put("mail.smtp.ssl.connectiontimeout", "60000");
            props.put("mail.smtp.ssl.timeout", "120000");
            props.put("mail.debug", "false");  // Change to "true" for debugging

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, DEFAULT_FROM_NAME));

            // Handle multiple recipients
            String[] recipientArray = recipients.split(",");
            InternetAddress[] addresses = new InternetAddress[recipientArray.length];
            for (int i = 0; i < recipientArray.length; i++) {
                addresses[i] = new InternetAddress(recipientArray[i].trim());
            }
            message.setRecipients(Message.RecipientType.TO, addresses);

            // Set subject and content
            message.setSubject(createEmailSubject(context));

            // Create multipart message
            Multipart multipart = new MimeMultipart();

            // HTML Email body
            MimeBodyPart htmlPart = new MimeBodyPart();
            String emailBody = createProfessionalEmailBody(context);
            htmlPart.setContent(emailBody, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);

            // ZIP Attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(zipFile);
            String attachmentName = "MSDAT-TestReport-" + getDateStamp() + ".zip";
            attachmentPart.setFileName(attachmentName);
            attachmentPart.setDescription("Complete test report bundle with HTML dashboard and screenshots");
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Send email
            Transport.send(message);

            System.out.println("✅ Professional test report email sent successfully!");
            System.out.println("📧 Recipients: " + recipients);
            System.out.println("📎 Attachment: " + attachmentName + " (" + formatFileSize(zipFile.length()) + ")");

        } catch (Exception e) {
            System.err.println("❌ Failed to send email report: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("   Cause: " + e.getCause().getMessage());
            }
            // Don't throw exception - email failure shouldn't break test execution
        }
    }

    /**
     * Upload report bundle to Google Drive using rclone
     * Requires rclone to be installed and configured
     *
     * @param zipFile The ZIP file to upload
     */
    public static void uploadToDrive(File zipFile) {
        try {
            String remotePath = getConfigValue("GDRIVE_REMOTE_PATH", "remote:TestReports/");

            ProcessBuilder pb = new ProcessBuilder("rclone", "copy", zipFile.getAbsolutePath(), remotePath);
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("☁️ Successfully uploaded report to Google Drive: " + remotePath);
            } else {
                System.err.println("❌ Failed to upload to Google Drive. Rclone exit code: " + exitCode);
                System.err.println("💡 Ensure rclone is installed and configured with 'rclone config'");
            }
        } catch (Exception e) {
            System.err.println("❌ Error uploading to Google Drive: " + e.getMessage());
            System.err.println("💡 Install rclone and configure with 'rclone config' to enable cloud upload");
        }
    }

    /**
     * Create ZIP archive from folder contents
     * Recursively includes all files and subdirectories
     *
     * @param sourceDirPath Source directory to zip
     * @param zipPath Output ZIP file path
     */
    public static void zipFolder(String sourceDirPath, String zipPath) {
        try (FileOutputStream fos = new FileOutputStream(zipPath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            Path sourcePath = Paths.get(sourceDirPath);

            if (!Files.exists(sourcePath)) {
                System.err.println("❌ Source directory does not exist: " + sourceDirPath);
                return;
            }

            Files.walk(sourcePath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        String zipEntryName = sourcePath.relativize(path).toString();
                        ZipEntry zipEntry = new ZipEntry(zipEntryName);
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            System.err.println("❌ Error adding file to ZIP: " + path + " - " + e.getMessage());
                        }
                    });

            System.out.println("✅ Successfully created ZIP archive: " + zipPath);

        } catch (IOException e) {
            System.err.println("❌ Failed to create ZIP archive: " + e.getMessage());
        }
    }

    // ================= PRIVATE HELPER METHODS =================

    /**
     * Get configuration value from environment variables with fallback to system properties
     */
    private static String getConfigValue(String envVar, String defaultValue) {
        // Try environment variable first (CI-friendly)
        String value = System.getenv(envVar);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        // Fallback to system property (IDE-friendly)
        String propKey = envVar.toLowerCase().replace("_", ".");
        value = System.getProperty(propKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        return defaultValue;
    }

    /**
     * Create professional email subject line
     */
    private static String createEmailSubject(ITestContext context) {
        if (context == null) {
            return "MSDAT Automation Report - " + getDateStamp();
        }

        int failed = context.getFailedTests().size();
        int passed = context.getPassedTests().size();
        String status = failed > 0 ? "❌ FAILED" : "✅ PASSED";
        String suiteName = context.getName();

        return String.format("MSDAT Test Report %s [%s] - %d/%d Tests - %s",
                status, suiteName, passed, (passed + failed), getDateStamp());
    }

    /**
     * Create comprehensive HTML email body
     */
    private static String createProfessionalEmailBody(ITestContext context) {
        if (context == null) {
            return createSimpleEmailBody();
        }

        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        int total = passed + failed + skipped;
        String overallStatus = failed > 0 ? "FAILED" : "PASSED";
        String statusClass = failed > 0 ? "status-failure" : "status-success";

        StringBuilder failedTestsHtml = new StringBuilder();
        if (failed > 0) {
            failedTestsHtml.append("<div class='failed-tests'>");
            failedTestsHtml.append("<h3>❌ Failed Test Details:</h3><ul>");
            for (ITestResult result : context.getFailedTests().getAllResults()) {
                String methodName = result.getMethod().getMethodName();
                String errorMsg = result.getThrowable() != null ?
                        result.getThrowable().getMessage() : "No error message available";
                failedTestsHtml.append(String.format("<li><strong>%s</strong><br><small>%s</small></li>",
                        methodName, errorMsg.length() > 100 ? errorMsg.substring(0, 100) + "..." : errorMsg));
            }
            failedTestsHtml.append("</ul></div>");
        }

        return String.format("""
            <html>
            <head>
                <meta charset="UTF-8">
                <title>MSDAT Test Report</title>
                %s
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🚀 MSDAT Daily Automation Report</h1>
                        <p>Comprehensive Test Execution Summary</p>
                    </div>
                    
                    <div class="content">
                        <h2>📊 Execution Summary</h2>
                        <p><strong>Overall Status:</strong> <span class="%s">%s</span></p>
                        <p><strong>Execution Date:</strong> %s</p>
                        <p><strong>Test Suite:</strong> %s</p>
                        <p><strong>Total Duration:</strong> %s</p>
                        
                        <table class="results-table">
                            <thead>
                                <tr>
                                    <th>✅ Passed</th>
                                    <th>❌ Failed</th>
                                    <th>⏭️ Skipped</th>
                                    <th>📊 Total</th>
                                    <th>📈 Success Rate</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="passed">%d</td>
                                    <td class="failed">%d</td>
                                    <td class="skipped">%d</td>
                                    <td><strong>%d</strong></td>
                                    <td><strong>%.1f%%</strong></td>
                                </tr>
                            </tbody>
                        </table>
                        
                        %s
                        
                        <div class="attachment-list">
                            <h3>📎 Report Attachments</h3>
                            <ul>
                                <li><strong>🌐 index.html</strong> - Interactive ExtentReports dashboard with detailed results, charts, and timeline</li>
                                <li><strong>📷 Screenshots</strong> - Visual evidence organized by test class and method</li>
                                <li><strong>📋 Summary Files</strong> - Machine-readable test execution data</li>
                            </ul>
                        </div>
                        
                        <div class="instructions">
                            <h3>💡 How to View Detailed Report</h3>
                            <ol>
                                <li>Download and extract the ZIP attachment</li>
                                <li>Open <code>index.html</code> in your web browser</li>
                                <li>Navigate through the interactive dashboard for detailed analysis</li>
                                <li>Click on test names to view logs and screenshots</li>
                            </ol>
                        </div>
                    </div>
                    
                    <div class="footer">
                        <p>🤖 This report was automatically generated by the MSDAT Test Automation Suite</p>
                        <p>Quality Assurance Team | Generated at %s</p>
                    </div>
                </div>
            </body>
            </html>
                """,
                EMAIL_STYLE,
                statusClass, overallStatus,
                getDateTimeStamp(),
                context.getName(),
                calculateDuration(context),
                passed, failed, skipped, total,
                total > 0 ? (passed * 100.0 / total) : 0.0,
                failedTestsHtml.toString(),
                getDateTimeStamp()
        );
    }

    /**
     * Create simple email body for cases without context
     */
    private static String createSimpleEmailBody() {
        return String.format("""
            <html>
            <head>
                <meta charset="UTF-8">
                <title>MSDAT Test Report</title>
                %s
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🚀 MSDAT Automation Report</h1>
                        <p>Test Execution Completed</p>
                    </div>
                    
                    <div class="content">
                        <h2>📊 Report Available</h2>
                        <p><strong>Execution Date:</strong> %s</p>
                        <p>Please find the attached test report bundle containing detailed results and screenshots.</p>
                        
                        <div class="attachment-list">
                            <h3>📎 Attachments Include</h3>
                            <ul>
                                <li><strong>🌐 HTML Dashboard</strong> - Interactive test results</li>
                                <li><strong>📷 Screenshots</strong> - Visual test evidence</li>
                                <li><strong>📋 Summary Data</strong> - Execution metadata</li>
                            </ul>
                        </div>
                        
                        <div class="instructions">
                            <h3>💡 How to View Report</h3>
                            <p>Download the ZIP attachment and open the HTML file in your browser for detailed results.</p>
                        </div>
                    </div>
                    
                    <div class="footer">
                        <p>🤖 Automated Test Report | MSDAT Quality Assurance</p>
                        <p>Generated at %s</p>
                    </div>
                </div>
            </body>
            </html>
                """, EMAIL_STYLE, getDateTimeStamp(), getDateTimeStamp());
    }

    /**
     * Calculate test execution duration
     */
    private static String calculateDuration(ITestContext context) {
        try {
            long startTime = context.getStartDate().getTime();
            long endTime = context.getEndDate().getTime();
            long durationMs = endTime - startTime;

            long minutes = durationMs / 60000;
            long seconds = (durationMs % 60000) / 1000;

            if (minutes > 0) {
                return String.format("%d min %d sec", minutes, seconds);
            } else {
                return String.format("%d sec", seconds);
            }
        } catch (Exception e) {
            return "Duration unavailable";
        }
    }

    /**
     * Format file size in human readable format
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }

    /**
     * Get current date stamp
     */
    private static String getDateStamp() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * Get current date and time stamp
     */
    private static String getDateTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}