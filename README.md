# MT Archiver - The Ultimate All-in-One Digital Everything App

**Package Name:** `dev.mtarchiver.app`  
**Target:** Android 8.0+ (minSdk 26, targetSdk 34)  
**Language:** Kotlin  
**UI Framework:** Jetpack Compose  
**Architecture:** MVVM + Repository + Modular Plugin System

## 🎯 Overview

MT Archiver is a comprehensive, enterprise-grade Android application that combines archive management, file management, terminal emulation, code editing, AI/ML, cloud storage, security tools, multimedia, and 100+ system utilities into a single, modular, extensible platform.

### Core Philosophy
- **Modular:** Each feature is a self-contained module with clear interfaces
- **Extensible:** Plugin system allows unlimited feature expansion
- **Enterprise-Ready:** SSO, RBAC, audit trails, compliance (GDPR, HIPAA, ISO 27001)
- **Developer-Friendly:** Open APIs for third-party plugins
- **Offline-First:** Works without internet; syncs when available
- **Security-First:** End-to-end encryption, Android Keystore, SQLCipher

## ✨ Tier 1: Core Features (MVP)

### 1.1 Archive Manager
- **Formats:** ZIP, 7z, TAR, GZIP, BZIP2, XZ, RAR (via plugin), ISO, Zstandard, custom `.mta`
- **Operations:** Create, extract, update, test, repair, split, merge
- **Encryption:** AES-256 with password manager
- **Performance:** Multi-threaded compression, streaming extraction

### 1.2 File Manager
- **UI:** Dual-panel, multi-tab, drag & drop
- **Features:** SAF support (Android 10+), root access (libsu), storage analysis, hex editor
- **Search:** Advanced filters (name, type, size, date, permissions)
- **Security:** File shredder (DoD 5220.22-M standard)

### 1.3 Terminal Emulator
- **Shell:** Linux shell (bash-compatible, via proot/Termux)
- **Features:** Multi-tab, split view, SSH/SFTP client, color schemes
- **Integration:** Direct file manager → terminal path
- **Scripts:** Run .sh, .py, .js scripts

### 1.4 Code Editor
- **Syntax:** 100+ language highlighting
- **IDE Features:** Auto-complete, refactoring, linting (LSP), multi-cursor, find & replace
- **Git:** Full Git integration (commit, push, pull, diff, branch) via JGit
- **Themes:** Dark/light, customizable
- **AI Integration:** AI-powered code completion and suggestions

### 1.5 AI Integration
- **On-Device:** TensorFlow Lite, ONNX Runtime (models: Code Llama, Stable Diffusion XL)
- **Cloud:** OpenAI GPT-4, Azure AI, Claude API
- **Features:**
  - AI file compression predictor
  - AI file organizer (auto-tagging, classification)
  - AI code assistant (autocomplete, bug detection)
  - AI security scanner (malware/anomaly detection)
  - AI chat for archive management

### 1.6 Cloud Integration
- **Providers:** Google Drive, OneDrive, Dropbox, Box, pCloud, Mega, S3, WebDAV, Nextcloud, SMB, FTP, SFTP
- **Auth:** OAuth2, SSO (SAML, OpenID Connect, LDAP, Kerberos)
- **Security:** E2E encryption, RBAC, audit trail

### 1.7 Enterprise Features
- **Authentication:** SSO, SAML, OAuth2, LDAP, Kerberos
- **Management:** RBAC, audit trail, compliance dashboard
- **Mobile Management:** MDM, remote wipe, kiosk mode, device policies

### 1.8 Plugin System
- **API:** ServiceLoader-based or custom class loader
- **Types:** Archive formats, cloud providers, editors, terminal backends, system tools
- **Distribution:** Built-in plugins (APK), external plugins (.jar/.dex files)
- **Marketplace:** Community plugin discovery and distribution

## 🚀 Tier 2: Pro/Extended Features

### System Management
Process Manager, Task Manager, Startup Manager, Package Manager, Permission Manager, Device Manager

### Network & Server
Download/Upload Manager, FTP/SFTP server/client, HTTP server, VPN (OpenVPN, WireGuard), network scanner, DLNA/UPnP, SMB, IoT (MQTT)

### Development
Python/Node.js/Ruby/Go/Rust compiler (proot-based), package managers (APT, pip, npm), database clients (SQLite, MySQL, PostgreSQL, MongoDB, Redis), Docker/K8s manager, CI/CD client, REST client (Postman-like)

### Multimedia & Office
Media player, image editor, PDF reader/editor, office suite (DOCX, XLSX, PPTX), markdown editor, ebook reader (EPUB, MOBI), comic reader (CBZ, CBR)

### Security
Password manager, encryption tools, PGP, hash calculator, digital signature, certificate manager, app lock (biometric), firewall, anti-malware, DLP

### Communication
Email client (IMAP, Exchange), chat (Matrix, XMPP, IRC), video conference (WebRTC), VoIP (SIP)

## 🛠 Technology Stack

| Component | Technology |
|-----------|----------|
| **Language** | Kotlin 1.9.20 |
| **UI** | Jetpack Compose 1.5.4 |
| **DI** | Hilt 2.48 |
| **Database** | Room 2.6.1 + SQLCipher |
| **Async** | Coroutines 1.7.3 + Flow |
| **Archive** | Apache Commons Compress, Zip4j, junrar, 7-Zip-JBinding, libzstd |
| **Terminal** | Termux library, libtermexec |
| **Editor** | Sora Editor (native) |
| **Git** | JGit |
| **AI/ML** | TensorFlow Lite, ONNX Runtime, OpenAI/Azure AI API |
| **Cloud SDK** | Official SDKs + SMBJ, sshj |
| **Root Access** | libsu |
| **Plugin** | ServiceLoader + custom ClassLoader |
| **Encryption** | Android Keystore, Tink, Bouncy Castle |
| **HTTP** | OkHttp 4.x + Retrofit 2.x |

## 📁 Project Structure

```
MT-Archiver/
├── app/                              # Main UI module (Jetpack Compose)
├── core-common/                      # Shared utilities
├── core-archive/                     # Archive abstraction layer
│   ├── format-zip/
│   ├── format-7z/
│   ├── format-tar/
│   ├── format-rar/
│   ├── format-iso/
│   └── format-custom-mta/
├── core-filemanager/                 # File manager
├── core-terminal/                    # Terminal emulator
├── core-editor/                      # Code editor
├── core-ai/                          # AI/ML module
├── core-cloud/                       # Cloud abstraction layer
│   ├── cloud-googledrive/
│   ├── cloud-onedrive/
│   ├── cloud-dropbox/
│   ├── cloud-smb/
│   └── cloud-sftp/
├── core-security/                    # Security & encryption
├── core-enterprise/                  # Enterprise features
├── core-network/                     # Network tools
├── core-multimedia/                  # Media & image
├── core-office/                      # Office & PDF
├── core-virtualization/              # proot, containers
├── core-communication/               # Email, chat, VoIP
├── core-utilities/                   # System utilities
├── plugin-api/                       # Plugin API & SPI
└── plugin-system/                    # Plugin loader & marketplace
```

## 🏗️ Building & Running

### Prerequisites
- Android Studio Jellyfish or later
- JDK 11 or higher
- Android SDK (API 34+)
- Gradle 8.0+

### Build
```bash
git clone https://github.com/Fyooriz/MT-Archiver.git
cd MT-Archiver

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run on device/emulator
./gradlew installDebug
adb shell am start -n dev.mtarchiver.app/.MainActivity
```

## 🗺️ Roadmap

### Phase 1 (MVP) - Month 1-2
- [ ] Archive Manager (ZIP, 7z, TAR)
- [ ] File Manager (basic + SAF)
- [ ] Plugin System (archive format plugins)
- [ ] Cloud Integration (Google Drive)
- [ ] Terminal Emulator (basic)

### Phase 2 (Extended) - Month 3-4
- [ ] Code Editor (Sora Editor + LSP)
- [ ] Git Integration
- [ ] AI Integration (TensorFlow Lite)
- [ ] Additional Cloud Providers (OneDrive, Dropbox)
- [ ] Network Tools (Download Manager, VPN)

### Phase 3 (Pro) - Month 5-6
- [ ] Enterprise Features (SSO, RBAC, audit)
- [ ] Security Features (encryption, password manager)
- [ ] Multimedia (media player, image editor)
- [ ] Office Suite (PDF, DOCX, XLSX)
- [ ] Advanced Terminal & Virtualization

### Phase 4 (Enterprise) - Month 7+
- [ ] MDM Integration
- [ ] Industry-Specific Modules
- [ ] Marketplace & Third-Party Plugins
- [ ] API Documentation & SDK

## 📚 Plugin Development Guide

See detailed documentation in `docs/PLUGIN_DEVELOPMENT.md`

## 🔐 Security & Privacy

- **Encryption:** AES-256 for archives, Android Keystore for keys, SQLCipher for local database
- **Authentication:** OAuth2, SAML, SSO for cloud
- **Audit Trail:** All sensitive operations logged to encrypted audit database
- **Data Minimization:** No telemetry; privacy-first approach
- **Compliance:** GDPR, HIPAA, ISO 27001 controls
- **Permissions:** Minimal permissions requested; runtime permissions for Android 6.0+

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Follow Kotlin code style (Ktlint)
4. Write unit tests
5. Submit a pull request

## 📄 License

MIT License - See [LICENSE.md](LICENSE.md)

## 💬 Support & Community

- **Issues:** [GitHub Issues](https://github.com/Fyooriz/MT-Archiver/issues)
- **Discussions:** [GitHub Discussions](https://github.com/Fyooriz/MT-Archiver/discussions)

---

**Made with ❤️ by Fyooriz and MT Archiver Contributors**
