import org.jsoup.Jsoup

data class nod(
    val url: String,
    val copii: MutableList<nod> = mutableListOf()
)

class WebCrawler(val domain: String)
{
    fun getLinks(url: String): List<String>
    {
        return try {
            val doc=Jsoup.connect(url)
                .timeout(3000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/120.0.0.0 Safari/537.36")
                .get()
            doc.select("a[href]").map{it.attr("abs:href")}
                .filter{it.contains(domain)}
                .distinct()
        }
        catch(e: Exception)
        {
            emptyList()
        }
    }

    fun buildTree(rootUrl: String, depth: Int, visited: MutableSet<String> = mutableSetOf()): nod
    {
        val node=nod(rootUrl)
        if(depth>0 && rootUrl !in visited)
        {
            visited.add(rootUrl)
            val links=getLinks(rootUrl).take(5)
            for(link in links)
            {
                val copil=buildTree(link, depth-1, visited)
                node.copii.add(copil)
            }
        }
        return node
    }

    fun serializeTree(node: nod, sb: StringBuilder)
    {
        val stringCopii=node.copii.joinToString(", "){it.url}
        sb.append("${node.url} | $stringCopii\n")
        node.copii.forEach{serializeTree(it, sb)}
    }

    fun deserializeTree(data: String): nod?
    {
        val lines=data.trim().lines()
        if(lines.isEmpty()) return null

        val nodesMap=mutableMapOf<String, nod>()

        lines.forEach{line ->
            if(line.contains("|"))
            {
                val parti=line.split("|")
                val urlParinte=parti[0].trim()
                val urlCopii=parti[1].split(",").map{it.trim()}.filter{it.isNotEmpty()}

                val nodParinte=nodesMap.getOrPut(urlParinte){nod(urlParinte)}

                urlCopii.forEach{urlCopil ->
                    val nodCopil=nodesMap.getOrPut(urlCopil){nod(urlCopil)}
                    if(!nodParinte.copii.any{it.url==urlCopil})
                    {
                        nodParinte.copii.add(nodCopil)
                    }
                }
            }
        }

        val firstLineUrl=lines[0].split("|")[0].trim()
        return nodesMap[firstLineUrl]
    }

    fun afiseazaArbore(node: nod, level: Int=0)
    {
        val indent="    ".repeat(level)
        println("$indent└── ${node.url}")
        node.copii.forEach{copil ->
            afiseazaArbore(copil, level+1)
        }
    }
}