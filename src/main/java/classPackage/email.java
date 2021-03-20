package classPackage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class email {

    public static void sendEmail(String email, String key, String fname, String action) {
        String to = email;
        String frontEnd = "";
        final String from = "pave.custserv@gmail.com";
        final String password = "adminp@ssw0rd";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (action.equals("verification")) {
                message.setSubject("PAVE Account Verification");
                frontEnd = verificationEmail(email, key, fname);
            } else if (action.equals("reset")) {
                message.setSubject("PAVE Reset Password");
                if (key == null || fname == null) {
                    frontEnd = resetEmail(email);
                } else {
                    frontEnd = resetEmail(email, key, fname);
                }
            }
            Multipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(frontEnd, "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println(e.getMessage());
        }
    }

    static String verificationEmail(String email, String key, String fname) {
        String design = "<head>\n"
                + "        <style>\n"
                + "            body {\n"
                + "                text-align: center;\n"
                + "                width :500px;\n"
                + "                background-color :white;\n"
                + "                margin-left: auto;\n"
                + "                margin-right: auto;\n"
                + "                line-height: 1.6\n"
                + "            }\n"
                + "            h1{\n"
                + "                font-family: \"Monaco\",Monospace;\n"
                + "                margin-top : 0px;\n"
                + "                margin-bottom: 10px;\n"
                + "            }\n"
                + "            h2{\n"
                + "                text-align: left;\n"
                + "                font-family: \"Helvetica\",Serif;\n"
                + "                margin-top : 10px;\n"
                + "                margin-bottom: 0px;\n"
                + "            }\n"
                + "            p{\n"
                + "                text-align: left;\n"
                + "                font-family : \"Helvetica\",Sans-serif;\n"
                + "            }\n"
                + "            .btn{\n"
                + "                color: black;\n"
                + "                font-size: 16px;\n"
                + "                background-color : #FA652B;\n"
                + "                padding : 10px 50px 10px 50px;\n"
                + "                vertical-align: middle;\n"
                + "            }\n"
                + "            .btn:hover{\n"
                + "                background-color : #FB5819;\n"
                + "            }\n"
                + "\n"
                + "        </style>\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Welcome to PAVE!</h1>\n"
                + "        <hr>\n"
                + "        <p>Hi " + fname + ",</p>\n"
                + "        <p>Before you enjoy the digital asset management services from PAVE, please verify your email address. Kindly click the button below to confirm your email address.</p>\n"
                + "        <p>Please ignore this email if you did not create a PAVE account using this email address.</p><br>\n"
                + "        <a class=\"btn\" href=\"http://pave.freeddns.org/emailServlet?key1=" + email + "&key2=" + key + "\">Verify email address</a>\n" //change the URL
                + "    </body>\n"
                + "";
        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Verification email sent to " + email);
        return design;
    }

    static String resetEmail(String email, String key, String fname) {
        String design = "<head>\n"
                + "        <style>\n"
                + "            body {\n"
                + "                text-align: center;\n"
                + "                width :500px;\n"
                + "                background-color :white;\n"
                + "                margin-left: auto;\n"
                + "                margin-right: auto;\n"
                + "                line-height: 1.6\n"
                + "            }\n"
                + "            h1{\n"
                + "                font-family: \"Monaco\",Monospace;\n"
                + "                margin-top : 0px;\n"
                + "                margin-bottom: 10px;\n"
                + "            }\n"
                + "            h2{\n"
                + "                text-align: left;\n"
                + "                font-family: \"Helvetica\",Serif;\n"
                + "                margin-top : 10px;\n"
                + "                margin-bottom: 0px;\n"
                + "            }\n"
                + "            p{\n"
                + "                text-align: left;\n"
                + "                font-family : \"Helvetica\",Sans-serif;\n"
                + "            }\n"
                + "            .btn{\n"
                + "                color: black;\n"
                + "                font-size: 16px;\n"
                + "                background-color : #FA652B;\n"
                + "                padding : 10px 50px 10px 50px;\n"
                + "                vertical-align: middle;\n"
                + "            }\n"
                + "            .btn:hover{\n"
                + "                background-color : #FB5819;\n"
                + "            }\n"
                + "\n"
                + "        </style>\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Password Reset</h1>\n"
                + "        <hr>\n"
                + "        <p>Hi " + fname + ",</p>\n"
                + "        <p>You recently requested to reset your password for your PAVE account. Click the button below to reset it.</p>\n"
                + "        <p>Please ignore this email if you did not create a PAVE account using this email address.</p><br>\n"
                + "        <a class=\"btn\" href=\"http://pave.freeddns.org/resetForgetPassword.jsp?key1=" + email + "&key2=" + key + "\">Reset Password</a>\n" //change the URL
                + "    </body>\n"
                + "";
        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Reset password email sent to " + email);
        return design;
    }

    static String resetEmail(String email) {
        String design = "<head>\n"
                + "        <style>\n"
                + "            body {\n"
                + "                text-align: center;\n"
                + "                width :500px;\n"
                + "                background-color :white;\n"
                + "                margin-left: auto;\n"
                + "                margin-right: auto;\n"
                + "                line-height: 1.6\n"
                + "            }\n"
                + "            h1{\n"
                + "                font-family: \"Monaco\",Monospace;\n"
                + "                margin-top : 0px;\n"
                + "                margin-bottom: 10px;\n"
                + "            }\n"
                + "            h2{\n"
                + "                text-align: left;\n"
                + "                font-family: \"Helvetica\",Serif;\n"
                + "                margin-top : 10px;\n"
                + "                margin-bottom: 0px;\n"
                + "            }\n"
                + "            p{\n"
                + "                text-align: left;\n"
                + "                font-family : \"Helvetica\",Sans-serif;\n"
                + "            }\n"
                + "            .btn{\n"
                + "                color: black;\n"
                + "                font-size: 16px;\n"
                + "                background-color : #FA652B;\n"
                + "                padding : 10px 50px 10px 50px;\n"
                + "                vertical-align: middle;\n"
                + "            }\n"
                + "            .btn:hover{\n"
                + "                background-color : #FB5819;\n"
                + "            }\n"
                + "\n"
                + "        </style>\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Password Reset</h1>\n"
                + "        <hr>\n"
                + "        <p>A password reset was requested for " + email + ". However there is no PAVE account associated with that address. If you're trying to reset your password, try again with a different email address</p>\n"
                + "        <p>You can also register your account with us by clicking the link below. Have a great day.</p><br>\n"
                + "        <a class=\"btn\" href=\"http://pave.freeddns.org/register.jsp\">Register</a>\n" //change the URL
                + "    </body>\n"
                + "";
        System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Reset password email sent to " + email + "[Email address not exists]");
        return design;
    }
}
