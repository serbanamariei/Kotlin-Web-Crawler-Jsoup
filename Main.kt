fun main()
{
    val domain="quotes.toscrape.com"
    val startUrl="http://quotes.toscrape.com"

    val crawler=WebCrawler(domain)
    val sb=StringBuilder()

    val arbore=crawler.buildTree(startUrl, 1)

    crawler.serializeTree(arbore, sb)
    val textSalvat=sb.toString()

    println("\nArbore serializat:")
    println(textSalvat)

    val arboreRefacut=crawler.deserializeTree(textSalvat)

    if(arboreRefacut!=null)
    {
        println("\nArbore deserializat:")
        println("Arborele a fost refacut cu succes!")
        println("Radacina: ${arboreRefacut.url}")
        println("Copii radacina: ${arboreRefacut.copii.size}")

        println("\nIerarhie:")
        crawler.afiseazaArbore(arboreRefacut)
    }
}