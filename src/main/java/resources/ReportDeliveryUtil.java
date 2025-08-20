    package resources;

    import jakarta.mail.*;
    import jakarta.mail.internet.*;
    import java.io.*;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.Properties;
    import java.util.zip.ZipEntry;
    import java.util.zip.ZipOutputStream;

    public class ReportDeliveryUtil {

        public static void emailZipReport(File zipFile) {
            final String username = "your.email@example.com";
            final String password = "your-email-password-or-app-password";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(
                        Message.RecipientType.TO, InternetAddress.parse("recipient@example.com"));
                message.setSubject("Automated Test Report ZIP");

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText("Please find the attached test report bundle.");

                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(zipFile);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                multipart.addBodyPart(attachmentPart);

                message.setContent(multipart);

                Transport.send(message);
                System.out.println("📧 Email sent successfully.");

            } catch (Exception e) {
                System.err.println("❌ Email sending failed: " + e.getMessage());
            }
        }


        public static void uploadToDrive(File zipFile) {
            try {
                // 'remote' is your rclone remote name configured to Google Drive
                ProcessBuilder pb = new ProcessBuilder("rclone", "copy", zipFile.getAbsolutePath(), "remote:TestReports/");
                pb.inheritIO(); // to see output/errors in console
                Process process = pb.start();
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    System.out.println("☁️ Uploaded ZIP report to Google Drive successfully.");
                } else {
                    System.err.println("❌ Failed to upload ZIP report, rclone exit code: " + exitCode);
                }
            } catch (Exception e) {
                System.err.println("❌ Error uploading to Google Drive: " + e.getMessage());
            }
        }


        public static void zipFolder(String sourceDirPath, String zipPath) {
            try (FileOutputStream fos = new FileOutputStream(zipPath);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                Path sourcePath = Paths.get(sourceDirPath);

                Files.walk(sourcePath)
                        .filter(path -> !Files.isDirectory(path))
                        .forEach(path -> {
                            ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                            try {
                                zos.putNextEntry(zipEntry);
                                Files.copy(path, zos);
                                zos.closeEntry();
                            } catch (IOException e) {
                                System.err.println("Error zipping file: " + path + " - " + e.getMessage());
                            }
                        });

            } catch (IOException e) {
                System.err.println("Error creating ZIP: " + e.getMessage());
            }
        }




    }
