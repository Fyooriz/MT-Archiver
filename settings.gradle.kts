pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "MT-Archiver"

include(
    ":app",
    ":core-common",
    ":core-archive",
    ":core-archive:format-zip",
    ":core-archive:format-7z",
    ":core-archive:format-tar",
    ":core-archive:format-rar",
    ":core-archive:format-iso",
    ":core-archive:format-custom-mta",
    ":core-filemanager",
    ":core-terminal",
    ":core-editor",
    ":core-ai",
    ":core-cloud",
    ":core-cloud:cloud-googledrive",
    ":core-cloud:cloud-onedrive",
    ":core-cloud:cloud-dropbox",
    ":core-cloud:cloud-smb",
    ":core-cloud:cloud-sftp",
    ":core-security",
    ":core-enterprise",
    ":core-network",
    ":core-multimedia",
    ":core-office",
    ":core-virtualization",
    ":core-communication",
    ":core-utilities",
    ":plugin-api",
    ":plugin-system"
)
