# Information Architecture

This document outlines the sitemap and navigation structure of the website, with a strong focus on supporting multi-lingual content.

## Multi-lingual Site Structure

AEM typically organizes multi-lingual sites using a language-rooted hierarchy.

- **Language Root**: Each language will have its own root page under `/content/{site-name}`.
    - `/content/{site-name}/en` (for English)
    - `/content/{site-name}/fr` (for French)
    - `/content/{site-name}/de` (for German)
- **Language Copies**: Content will be created in a master language (e.g., English) and then translated into other languages using AEM's translation workflows and tools (e.g., translation memory, machine translation).
- **Language Navigation**: A language switcher component will allow users to easily navigate between different language versions of a page.

## Sitemap (Example for English)

- `/content/{site-name}/en` (Home - English)
- `/content/{site-name}/en/products`
    - `/content/{site-name}/en/products/{product-name}`
- `/content/{site-name}/en/solutions`
    - `/content/{site-name}/en/solutions/{solution-name}`
- `/content/{site-name}/en/about-us`
- `/content/{site-name}/en/contact`

## Navigation (Example for English Main Navigation)

### Main Navigation

- Home
- Products
    - Product A
    - Product B
- Solutions
    - Solution X
    - Solution Y
- About Us
- Contact

### Footer Navigation

- Privacy Policy
- Terms of Service
- Social Media Links
- Language Switcher (links to equivalent pages in other languages)
