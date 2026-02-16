# Dispatcher Configuration

This directory contains the Apache/Dispatcher configuration for AEM as a Cloud Service.

## Structure

```
dispatcher/
└── src/
    ├── conf.d/                    # Apache configuration
    │   ├── available_vhosts/      # Virtual host configurations
    │   ├── enabled_vhosts/        # Symlinks to active vhosts
    │   ├── rewrites/              # URL rewrite rules
    │   └── variables/             # Environment variables
    │
    └── conf.dispatcher.d/         # Dispatcher configuration
        ├── available_farms/       # Farm configurations
        ├── enabled_farms/         # Symlinks to active farms
        ├── filters/               # Request filter rules
        ├── cache/                 # Cache rules
        ├── clientheaders/         # Headers to pass to AEM
        └── renders/               # AEM render configuration
```

## Local Testing

To test dispatcher configuration locally:

1. **Install Docker**

2. **Run the dispatcher SDK:**
   ```bash
   # Download dispatcher SDK from Software Distribution
   # Extract and run:
   ./bin/docker_run.sh <aem-publish-host> <aem-publish-port> <dispatcher-src-folder>

   # Example:
   ./bin/docker_run.sh localhost 4503 ./src
   ```

3. **Validate configuration:**
   ```bash
   ./bin/validate.sh ./src
   ```

## Key Configuration Files

| File | Purpose |
|------|---------|
| `available_vhosts/default.vhost` | Main Apache virtual host |
| `rewrites/rewrite.rules` | URL rewriting (short URLs, redirects) |
| `filters/filters.any` | Security filters (allow/deny URLs) |
| `cache/rules.any` | What to cache and invalidate |

## Common Tasks

### Add a New URL Rewrite

Edit `conf.d/rewrites/rewrite.rules`:
```apache
RewriteRule ^/old-path$ /new-path [R=301,L]
```

### Allow a New Path

Edit `conf.dispatcher.d/filters/filters.any`:
```
/0200 { /type "allow" /url "/my-new-path/*" }
```

### Disable Caching for a Path

Edit `conf.dispatcher.d/cache/rules.any`:
```
/0040 { /glob "/content/*/dynamic/*" /type "deny" }
```

## Cloud Manager Deployment

Configuration is deployed automatically via Cloud Manager pipeline.
Validation runs during the build phase.

See: [Dispatcher in the Cloud](https://experienceleague.adobe.com/docs/experience-manager-cloud-service/content/implementing/content-delivery/disp-overview.html)
