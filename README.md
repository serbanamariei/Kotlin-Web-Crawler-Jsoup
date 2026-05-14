# 🕷️ Kotlin Web Crawler

A recursive web crawler implemented in Kotlin that builds a tree of links starting from a given URL, with support for serialization and deserialization of the resulting tree. Developed as part of Lab 3 — Simple ADT Applications in Kotlin.

---

## Project Structure

```
.
└── Main.kt        # All classes and entry point
```

---

## Requirements

- Kotlin (JVM)
- Maven
- Jsoup library

Add the following to your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.jsoup</groupId>
        <artifactId>jsoup</artifactId>
        <version>1.13.1</version>
    </dependency>
</dependencies>
```

---

## How to Run

```bash
mvn compile
mvn exec:java -Dexec.mainClass="MainKt"
```

---

## How It Works

### Tree Structure (`nod`)

The crawled links are stored as a tree where each node holds a URL and a mutable list of child nodes. The tree is built recursively starting from the root URL.

```kotlin
data class nod(
    val url: String,
    val copii: MutableList<nod> = mutableListOf()
)
```

### Crawling (`getLinks`)

For a given URL, `getLinks` uses **Jsoup** to fetch the page and extract all anchor tags (`a[href]`). The results are filtered to keep only links that belong to the same domain, and duplicates are removed.

If the request fails for any reason (timeout, connection error, invalid URL), the function returns an empty list and the crawl continues without crashing.

### Building the Tree (`buildTree`)

`buildTree` is a recursive function that builds the link tree up to a given depth. At each level it:
- Marks the current URL as visited to avoid infinite loops
- Fetches up to 5 child links from the current page
- Recursively builds a subtree for each child

With `depth=1`, the crawler fetches the root URL and all links found on it, but does not go deeper. With `depth=2`, it also follows each of those links one level further — a **depth-first traversal**.

### Serialization (`serializeTree`)

The tree is serialized to a plain text format where each line represents one node:

```
<parent_url> | <child1_url>, <child2_url>, ...
```

The function traverses the tree recursively and appends each node's line to a `StringBuilder`. The result can be printed, saved to a file, or transmitted over a network.

**Example output:**
```
http://quotes.toscrape.com | http://quotes.toscrape.com/page/2/, http://quotes.toscrape.com/tag/love/
http://quotes.toscrape.com/page/2/ | http://quotes.toscrape.com/page/3/
```

### Deserialization (`deserializeTree`)

The serialized text is parsed back into a tree. Each line is split on `|` to recover the parent URL and its children. All nodes are stored in a `MutableMap<String, nod>` keyed by URL, so nodes referenced multiple times are reused rather than duplicated. The root is identified as the parent on the first line.

### Tree Display (`afiseazaArbore`)

Prints the tree to the console with indentation to visualize the hierarchy:

```
└── http://quotes.toscrape.com
    └── http://quotes.toscrape.com/page/2/
        └── http://quotes.toscrape.com/page/3/
    └── http://quotes.toscrape.com/tag/love/
```

---

## Example Output

```
Arbore serializat:
http://quotes.toscrape.com | http://quotes.toscrape.com/page/2/, ...

Arbore deserializat:
Arborele a fost refacut cu succes!
Radacina: http://quotes.toscrape.com
Copii radacina: 5

Ierarhie:
└── http://quotes.toscrape.com
    └── http://quotes.toscrape.com/page/2/
    └── http://quotes.toscrape.com/tag/love/
    ...
```

---

## Tech Stack

| Component | Technology |
|---|---|
| Language | Kotlin (JVM) |
| Build Tool | Maven |
| HTML Parsing | Jsoup 1.13.1 |
