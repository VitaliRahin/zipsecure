package com.zipsecure.settings;

import com.beust.jcommander.Parameter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CliArgs {
    @Parameter(
            names = "--pathSettings",
            description = "Config file path"
    )
    private String pathSettings = "src/main/resources/settings.json";
    @Parameter(
            names = "--pathBackup",
            description = "Path to archives"
    )
    private String destinationPath = "zip/";
    @Parameter(
            names = "--pathZip",
            description = "Path to backup before archiving"
    )
    private String backupPath = "backup/";
    @Parameter(
            names = "--publicKey",
            description = "Path to public key"
    )
    private String publicKeyPath = "public.der";
}
