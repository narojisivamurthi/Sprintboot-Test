# 📦 NPM & Package Management Master Roadmap & Learning Progress Tracker

## 🏛️ Package Manager Architecture & Dependency Resolution

### 🏗️ NPM Dependency Resolution Architecture
```mermaid
graph TD
    Manifest["📄 package.json Manifest"] --> Lockfile["🔒 package-lock.json (Deterministic Tree & Hashes)"]

    Lockfile --> Resolver["🔍 NPM Dependency Resolver Algorithm"]

    subgraph Tree ["📂 node_modules Directory (Flat Tree)"]
        DirectDep["📦 Direct Dependency A (v2.0)"]
        SharedDep["📦 Transitive Dependency B (v1.5)"]
    end

    Resolver --> Tree
```

### 🔄 Semantic Versioning (SemVer) Resolution Sequence
```mermaid
sequenceDiagram
    autonumber
    actor Dev as Developer / CI Runner
    participant NPM as NPM CLI
    participant Lock as package-lock.json
    participant Reg as NPM Registry (registry.npmjs.org)

    Dev->>NPM: npm install lodash@^4.17.0
    NPM->>Lock: Check lockfile for locked exact version
    alt Lockfile exists & valid
        Lock-->>NPM: Return exact version (4.17.21) + SHA-512 Hash
    else Lockfile missing or new package
        NPM->>Reg: Fetch package metadata for ^4.17.0
        Reg-->>NPM: Return latest matching minor version (4.17.21)
        NPM->>Lock: Generate & lock exact version 4.17.21 with integrity hash
    end
    NPM->>Dev: Download tarball, verify hash, & extract to node_modules
```

---

## 📚 Exhaustive NPM Commands Reference Matrix

| Category | Command | Description & Typical Usage |
| :--- | :--- | :--- |
| **Initialization** | `npm init` | Interactively creates a new `package.json` file |
| **Initialization** | `npm init -y` | Initializes `package.json` accepting all default values |
| **Install (Production)**| `npm install <pkg>` / `npm i <pkg>` | Installs package into `dependencies` and updates `package-lock.json` |
| **Install (Dev)** | `npm install -D <pkg>` / `npm i --save-dev <pkg>` | Installs package into `devDependencies` (tools, testing, build) |
| **Install (Global)** | `npm install -g <pkg>` | Installs CLI package globally on system |
| **Install (Exact)** | `npm install --save-exact <pkg>` | Installs package without `^` or `~` caret operators in `package.json` |
| **Install (Clean CI)**| `npm ci` | Deletes `node_modules` and installs strictly from `package-lock.json` (CI/CD) |
| **Package Removal** | `npm uninstall <pkg>` / `npm rm <pkg>` | Removes package from `node_modules` and `package.json` |
| **Package Removal** | `npm uninstall -g <pkg>` | Removes globally installed package |
| **Update & Outdated**| `npm outdated` | Checks registry to list packages with newer versions available |
| **Update & Outdated**| `npm update` / `npm up` | Updates all packages to newest version obeying SemVer constraints |
| **Update & Outdated**| `npm update <pkg>` | Updates specific package to latest matching version |
| **Execution** | `npm run <script>` | Runs a custom script defined in `package.json` `scripts` object |
| **Execution** | `npm test` / `npm t` | Alias shortcut to run `npm run test` |
| **Execution** | `npm start` | Alias shortcut to run `npm run start` |
| **Execution** | `npx <command>` | Executes CLI package binary without installing it globally |
| **Local Symlinking** | `npm link` | Creates global symlink of current package folder for local testing |
| **Local Symlinking** | `npm link <pkg-name>` | Links globally symlinked package into active project's `node_modules` |
| **Unlinking** | `npm unlink` | Removes global symlink created by `npm link` |
| **Workspaces** | `npm run <script> -w <pkg>` | Runs npm script inside a specific workspace sub-package |
| **Workspaces** | `npm run <script> --workspaces` | Runs npm script across ALL workspace sub-packages in parallel |
| **Workspaces** | `npm i <pkg> -w <workspace-name>` | Installs package into a specific workspace sub-package |
| **Security & Audits**| `npm audit` | Scans dependency tree against known CVE vulnerability databases |
| **Security & Audits**| `npm audit fix` | Automatically updates vulnerable dependencies to safe versions |
| **Security & Audits**| `npm audit fix --force` | Force updates dependencies to fix vulnerabilities (may introduce breaking changes) |
| **Registry & Auth** | `npm login` | Authenticates user credentials with NPM Registry |
| **Registry & Auth** | `npm logout` | Logs out user from active registry session |
| **Registry & Auth** | `npm whoami` | Displays currently authenticated NPM username |
| **Registry & Auth** | `npm config get registry` | Returns current active registry URL (`registry.npmjs.org`) |
| **Publishing** | `npm publish` | Publishes package to public NPM registry |
| **Publishing** | `npm publish --access public` | Publishes scoped package (`@myorg/my-pkg`) publicly |
| **Publishing** | `npm publish --dry-run` | Simulates package publishing without uploading tarball to registry |
| **Publishing** | `npm pack` | Creates `.tgz` tarball asset locally exactly as it would be published |
| **Version Bumping** | `npm version patch` | Bumps `PATCH` version ($1.0.0 \rightarrow 1.0.1$) and creates Git tag |
| **Version Bumping** | `npm version minor` | Bumps `MINOR` version ($1.0.0 \rightarrow 1.1.0$) and creates Git tag |
| **Version Bumping** | `npm version major` | Bumps `MAJOR` version ($1.0.0 \rightarrow 2.0.0$) and creates Git tag |
| **Cache & Cleanup** | `npm cache clean --force` | Clears local NPM download cache |
| **Cache & Cleanup** | `npm cache verify` | Verifies integrity of cached package data |
| **Cache & Cleanup** | `npm prune` | Removes extraneous packages in `node_modules` not in `package.json` |
| **Cache & Cleanup** | `npm dedupe` | Flatten and deduplicate identical transitive dependency sub-trees |
| **Inspection** | `npm list` / `npm ls` | Displays installed dependency tree |
| **Inspection** | `npm list --depth=0` | Displays top-level direct dependencies only |
| **Inspection** | `npm info <pkg>` / `npm view <pkg>` | Shows package metadata, versions, and dependencies from registry |

---

## 📑 Phase 1: NPM Core & Package Architecture

### Module 1: Introduction to NPM & Node Package Ecosystem
- [x] **What is NPM?**
  - Default package manager for Node.js comprising a CLI tool and the central NPM Registry (`registry.npmjs.org`).
- [x] **The Node Package Ecosystem**
  - World's largest software registry distributing reusable open-source JavaScript libraries and CLI tools.

### Module 2: `package.json` Anatomy & Fields
- [x] **`dependencies` vs `devDependencies`**
  - `dependencies`: Libraries required for production runtime (e.g. `express`, `mongoose`).
  - `devDependencies`: Tools required only during development/testing (e.g. `jest`, `typescript`, `nodemon`).
- [x] **`peerDependencies` & `optionalDependencies`**
  - `peerDependencies`: Expresses compatibility requirements for plugins without bundling them (e.g. React component requiring host `react`).
  - `optionalDependencies`: Non-critical packages where installation failure does not break the build.
- [x] **Metadata & Entry Points (`main`, `module`, `bin`, `scripts`)**
  - `main`: Entry point for CommonJS (`dist/index.js`); `module`: Entry point for ES Modules; `bin`: Executable CLI scripts; `scripts`: Command shortcuts.

### Module 3: Semantic Versioning (SemVer)
- [x] **SemVer Format (`MAJOR.MINOR.PATCH`)**
  - `MAJOR`: Breaking breaking changes.
  - `MINOR`: New backward-compatible functionality.
  - `PATCH`: Backward-compatible bug fixes.
- [x] **Version Range Operators (`^`, `~`, exact, wildcard)**
  - Caret (`^1.2.3`): Allows updates to higher `MINOR` and `PATCH` versions ($< 2.0.0$).
  - Tilde (`~1.2.3`): Allows updates only to higher `PATCH` versions ($< 1.3.0$).
  - Exact (`1.2.3`): Locks strictly to version `1.2.3`.

### Module 4: `package-lock.json` vs `package.json`
- [x] **`package-lock.json` Purpose**
  - Automatically generated lockfile guaranteeing identical, deterministic `node_modules` trees across all machines.
- [x] **`npm ci` vs `npm install`**
  - `npm install`: Evaluates `package.json`, updates `package-lock.json`, and installs dependencies.
  - `npm ci` (Clean Install): Deletes `node_modules`, installs strictly from `package-lock.json`, fails if out of sync with `package.json`. Ideal for CI/CD pipelines!

---

## ⚡ Phase 2: CLI Commands, Scripts & Workspaces

### Module 5: Essential NPM CLI Commands
- [x] **Core Management Commands**
  - `npm init` (initializes manifest), `npm install` (`npm i`), `npm update`, `npm uninstall`, `npm prune` (removes extraneous packages), `npm dedupe`.
- [x] **NPM Link (`npm link`)**
  - Creates a global symlink between local package development folders to test unpublished packages locally.

### Module 6: NPM Scripts & Lifecycle Hooks
- [x] **Script Execution & Lifecycle Hooks**
  - Running scripts via `npm run <script-name>`.
  - Automatic pre/post hooks: `preinstall`, `postinstall` (runs post-download build scripts), `pretest`, `test`, `posttest`.
- [x] **`npx` (Package Runner)**
  - Executes binaries from local or temporary remote packages without globally installing them (`npx create-react-app`).

### Module 7: NPM Workspaces & Monorepos
- [x] **NPM Workspaces (`workspaces` field)**
  - Native feature managing multiple sub-packages within a single root monorepository.
  - Symlinks sub-packages together and hoists shared dependencies to root `node_modules`.

---

## 🛠️ Phase 3: Package Managers & Publishing

### Module 8: Package Manager Comparisons (NPM vs Yarn vs pnpm)
- [x] **NPM vs Yarn vs pnpm**
  - **NPM**: Default built-in tool using flat `node_modules` layout.
  - **Yarn**: Fast parallel downloading with offline caching.
  - **pnpm**: Uses a global content-addressable store and **hard links/symlinks**, saving up to 70% disk space over NPM.

### Module 9: Publishing Packages to NPM Registry
- [x] **Publishing Workflow**
  - `npm login`, `npm publish`, using `.npmignore` to exclude test files, publishing scoped packages (`@myorg/my-pkg`).

### Module 10: Security Auditing & Supply Chain Protection
- [x] **`npm audit` & Supply Chain Attacks**
  - `npm audit fix` scans dependencies against known CVE databases.
  - Mitigating typosquatting, malicious `postinstall` hooks, and lockfile poisoning.

---

## 🛠️ Phase 4: Practical Configuration Snippets

### 1. `package.json` Monorepo Workspaces & Scripts Configuration
```json
{
  "name": "my-monorepo",
  "version": "1.0.0",
  "private": true,
  "workspaces": [
    "packages/*"
  ],
  "scripts": {
    "build": "npm run build --workspaces",
    "test": "npm run test --workspaces",
    "preinstall": "npx only-allow pnpm"
  },
  "devDependencies": {
    "typescript": "^5.0.0"
  }
}
```

### 2. Custom `.npmrc` Security & Auth Configuration
```ini
# Force exact version saving on npm install
save-exact=true

# Custom registry scoping
@mycompany:registry=https://npm.pkg.github.com

# Strict SSL check and timeout
strict-ssl=true
fetch-timeout=60000
```

---

## 🎯 Top NPM Interview Q&A Cheatsheet (Master List)

### Q1: What is the difference between `package.json` and `package-lock.json`?
`package.json` specifies broad version ranges using SemVer operators (`^`, `~`). `package-lock.json` locks the exact resolved version, exact dependency tree hierarchy, and cryptographic SHA-512 integrity hashes for every package, ensuring deterministic builds across machines.

### Q2: When should you use `npm ci` instead of `npm install`?
Use `npm ci` in automated CI/CD build environments. `npm ci` deletes existing `node_modules`, installs strictly from `package-lock.json` without modifying it, and throws an error if `package-lock.json` is out of sync with `package.json`, guaranteeing faster and reproducible builds.

### Q3: What is the difference between Caret (`^`) and Tilde (`~`) in SemVer?
- Caret (`^1.2.3`): Allows backward-compatible updates to higher MINOR and PATCH versions ($< 2.0.0$).
- Tilde (`~1.2.3`): Allows backward-compatible updates only to higher PATCH versions ($< 1.3.0$).

### Q4: How does pnpm differ from NPM and Yarn?
pnpm uses a global content-addressable storage on disk. Instead of duplicating package files in every project's `node_modules`, pnpm creates hard links and symlinks pointing to single stored files, saving gigabytes of disk space and preventing phantom dependency access.

### Q5: What are `peerDependencies` and when should you use them?
`peerDependencies` express a requirement that the host application must install a specific version of a dependency (e.g. a plugin requiring `react@^18.0.0`). The plugin itself does not bundle the dependency, avoiding duplicate instances at runtime.
