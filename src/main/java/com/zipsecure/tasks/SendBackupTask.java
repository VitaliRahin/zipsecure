package com.zipsecure.tasks;

import com.zipsecure.secure.Encrypt;
import com.zipsecure.utils.SendEmail;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimerTask;

import static com.zipsecure.secure.PasswordGenerator.autoGeneratePassword;
import static com.zipsecure.utils.FileUtils.*;
import static com.zipsecure.utils.Network.isConnected;
import static com.zipsecure.utils.ZipUtils.createSplitZipFile;

public class SendBackupTask extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger(SendBackupTask.class);
    private static final String GMAIL_URL = "www.googleapis.com";
    private static final String BODY_EMAIL = "Please put all files to new directory and execute decrypting script";
    private String backupsPath;
    private String destinationPath;
    private String credentialsFilePath;
    private String publicKeyPath;
    private int splitSize;
    private String email;


    private SendBackupTask(Builder builder){
        this.backupsPath = builder.backupsPath;
        this.destinationPath = builder.destinationPath;
        this.credentialsFilePath = builder.credentialsFilePath;
        this.publicKeyPath = builder.publicKeyPath;
        this.splitSize = builder.splitSize;
        this.email = builder.email;
    }

    public void run() {

        LOGGER.info("Task was running at " + LocalDateTime.now());
        if (!isConnected(GMAIL_URL)) {
            LOGGER.error("Cannot connect to the Internet. Try again in the next time");
            return;
        }
        Encrypt encrypt = Encrypt.getInstance(publicKeyPath);

        File[] filesArray = Optional.ofNullable(new File(backupsPath).listFiles()).orElse(new File[0]);
        for (File file : filesArray) {
            List<String> filePathToDelete = new ArrayList<>();

            char[] password = autoGeneratePassword();
            String zipName = generateZipName(file, destinationPath);
            filePathToDelete.add(file.getAbsolutePath());
            try {
                ZipFile zipFile = createSplitZipFile(file.getAbsolutePath(), zipName, password, splitSize);
                String pathPass = writeBytesToFile(encrypt.encryptPassword(password), destinationPath + "password");
                LOGGER.info("Created zip file " + zipFile.getFile().getName());
                List<String> filePathToSend = new ArrayList<>(zipFile.getSplitZipFiles());
                filePathToSend.add(pathPass);
                sendEmail(credentialsFilePath, filePathToSend, email);
                filePathToDelete.addAll(filePathToSend);

            } catch (ZipException | IOException | MessagingException
                    e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
            deleteFiles(filePathToDelete);
            LOGGER.info("Files were deleted");
        }
        LOGGER.info("Task was completed at " + LocalDateTime.now());
    }

    private void sendEmail(String credentialsFilePath, List<String> filePathToSend, String email) throws IOException, MessagingException {
        SendEmail sendEmail = new SendEmail(credentialsFilePath);
        for (int i = 0; i < filePathToSend.size(); i++ ){
            sendEmail.createAndSendEmailWithAttachment(email, email, prepareSubjectEmail(i, filePathToSend.size()),
                    BODY_EMAIL, new File(filePathToSend.get(i)));
        }
    }

    private String prepareSubjectEmail(int iteration, int total){
        return "Backup " + LocalDate.now() + " Email " + (iteration + 1) + " From " + total;
    }


    public static class Builder{
        private String backupsPath;
        private String destinationPath;
        private String credentialsFilePath;
        private String publicKeyPath;
        private int splitSize;
        private String email;

        public Builder(String backupsPath, String destinationPath) {
            this.backupsPath = backupsPath;
            this.destinationPath = destinationPath;
        }
        public Builder credentialsFilePath (String credentialsFilePath){
            this.credentialsFilePath = credentialsFilePath;
            return this;
        }
        public Builder publicKeyPath (String publicKeyPath){
            this.publicKeyPath = publicKeyPath;
            return this;
        }
        public Builder splitSize (int splitSize){
            this.splitSize = splitSize;
            return this;
        }
        public Builder email (String email){
            this.email = email;
            return this;
        }

        public SendBackupTask build(){
            return new SendBackupTask(this);
        }
    }

}
