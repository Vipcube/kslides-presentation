import com.kslides.*
import com.pambrose.srcref.Api.srcrefUrl
import kotlinx.css.h3
import kotlinx.css.img
import kotlinx.html.*

fun main() {
    val slides = "src/main/kotlin/Slides.kt"

    fun srcrefLink(token: String, escapeHtml4: Boolean = false) =
        srcrefUrl(
            account = "kslides",
            repo = "kslides-template",
            path = slides,
            beginRegex = "\\s+// $token begin",
            beginOffset = 1,
            endRegex = "\\s+// $token end",
            endOffset = -1,
            escapeHtml4 = escapeHtml4,
        )

    kslides {
        output {
            // Write the presentation's html files to /docs for GitHub Pages or netlify.com
            enableFileSystem = true

            // Run locally or on Heroku
            enableHttp = true
        }

        // CSS values assigned here are applied to all the presentations
        css += """
              #githubCorner path { 
                fill: #258BD2; 
              }
              
              .image75 {
                width: 75%;
                height: 75%;
              }
              """

        presentationConfig {
            history = true
            transition = Transition.SLIDE
            transitionSpeed = Speed.SLOW

            topLeftHref = "https://github.com/Vipcube/kslides-presentation/" // Assign to "" to turn this off
            topLeftTitle = "View presentation source on Github"

            topRightHref = "/" // Assign to "" to turn this off
            topRightTitle = "Go to 1st Slide"
            topRightText = "🏠"

            enableMenu = true
            theme = PresentationTheme.SOLARIZED
            slideNumber = "c/t"

            menuConfig {
                numbers = true
            }

            copyCodeConfig {
                timeout = 2000
                copy = "Copy"
                copied = "Copied!"
            }

            slideConfig {
                // Assign slide config defaults for all presentations
                // backgroundColor = "blue"
            }
        }

        presentation {
            markdownSlide {
                id = "main"
                slideConfig {
                    transition = Transition.ZOOM
                }

                content {
                    """
                    ### Brad Chen
                    ### 投影片目錄
                     
                    - [Database](/database) 
                    
                    """
                }
            }
        }

        // Database
        presentation {
            path = "database"

            presentationConfig {
                topRightHref = "/#/main"
                topRightTitle = "Go back to catalog page"
                topRightText = "🔙"
            }

            dslSlide {
                content {
                    h3 { +"Database" }
                    ul {
                        li {
                            a {
                                href = "/database_performance"
                                +"Database Performance"
                            }
                        }
                    }
                }
            }
        }

        presentation {
            path = "database_performance"

            presentationConfig {
                topRightHref = "/database"
                topRightTitle = "Go back to database page"
                topRightText = "🔙"
            }

            dslSlide {
                content {
                    h3 { +"Database Performance" }
                    ul {
                        li {
                            +"Index"
                        }
                        li {
                            a {
                                href = "/database_replication"
                                +"Replication"
                            }
                        }
                        li {
                            +"Partitioning"
                        }
                    }
                }
            }
        }

        presentation {
            path = "database_replication"

            presentationConfig {
                topRightHref = "/database_performance"
                topRightTitle = "Go back to database performance page"
                topRightText = "🔙"
            }

            verticalSlides {
                markdownSlide {
                    content {
                        """
                        ### Database Replication

                        ![Master Slave](images/database/database-replication-master-slave.png)
                        
                        原作者：[Database Replication](https://homuchen.com/posts/what-and-why-database-replication-advantage-and-disadvantage/)
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### 為何需要 Replication?

                        - 資料備份
                        - 增進讀取效能
                           - Read throughput： 查詢分散至 N 台機器
                           - Read Latency： 查詢最近的機器
                        - 基於以上，可實作讀寫分離
                        - 資料庫高可用性
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### Replication 缺點

                        - 磁碟空間增長
                        - 需維護資料庫 Cluster，整體複雜度上升
                        - 資料同步延遲及不一致性
                            - Replication Lag
                            - Concurrent Write
                        """
                    }
                }

                dslSlide {
                    content {
                        h3 { +"Read Your Write" }
                        img { src = "images/database/database-replication-read-your-write.png"; classes =
                            setOf("image75")
                        }
                        ul {
                            li {
                                +"寫入至 Master"
                            }
                            li {
                                +"立即讀取 Slave"
                            }
                        }
                    }
                }

                dslSlide {
                    content {
                        h3 { +"Monotonic Read" }
                        img { src = "images/database/database-replication-monotonic-read.png"; classes =
                            setOf("image75")
                        }
                        ul {
                            li {
                                +"連續讀取，讀到最新的資料後，接著又讀到舊的資料"
                            }
                            li {
                                +"這種情況會發生於不知道是從 Master 還是 Slave 中讀取資料"
                            }
                        }
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### 如何解決 (連線層)
                        
                        - 僅從 Master 讀取
                        - Transactional Read/Write，皆從 Master 讀取
                        - 可以保證 read your write consistency
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### 如何達到 (軟體層)
                        
                        - Native： 透過程式碼實作，直接指定讀取的資料庫
                        - Client Dependency： 透過第三方套件，達到功能
                        - Infra Proxy： 透過 Infra，達到功能
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### Spring Boot Demo
                        
                        - [GitHub Source](https://github.com/Vipcube/Demo-SpringBoot-DB-ReadWriteSplitting)
                        - 實作採用 JPA 
                        - 介紹 Native 實作 
                        - 介紹 Apache ShardingSphere Proxy
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### Apache ShardingSphere
                        
                        - [Documents](https://shardingsphere.apache.org/document/current/en/overview/)
                        - Client Dependency： ShardingSphere-JDBC
                        - Infra Proxy： ShardingSphere-Proxy
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### ShardingSphere-Proxy
                        
                        ![Proxy](https://shardingsphere.apache.org/document/current/img/shardingsphere-proxy_v2.png)
                        """
                    }
                }

                markdownSlide {
                    val src = "src/main/java/org/vipcube/spring/demo/config/DataSourceConfig.java"
                    content {
                        """
                        ### Native Config
                        ```java [4,5|10|16,17]
                        ${include(githubRawUrl("Vipcube", "Demo-SpringBoot-DB-ReadWriteSplitting", src), "[65-83]")}
                        ```
                        """
                    }
                }
            }
        }
//
//            verticalSlides {
//                // code2 begin
//                dslSlide {
//                    val src = "kslides-examples/src/main/kotlin/content/HelloWorldK.kt"
//                    val url = githubRawUrl("kslides", "kslides", src)
//                    content {
//                        h2 { +"Code with a dslSlide" }
//                        // Display lines 3-7 of the url content and highlight lines 1 and 5, 2 and 4, and finally 3
//                        codeSnippet {
//                            language = "kotlin"
//                            highlightPattern = "[1,5|2,4|3]"
//                            +include(url, "[3-7]")
//                        }
//                    }
//                }
//                // code2 end
//
//                dslSlide {
//                    content {
//                        h2 { +"Slide Definition" }
//                        codeSnippet {
//                            language = "kotlin"
//                            +include(slides, beginToken = "code2 begin", endToken = "code2 end")
//                        }
//                        a {
//                            id = "ghsrc"
//                            href = srcrefLink("code2")
//                            target = "_blank"
//                            +"GitHub Source"
//                        }
//                    }
//                }
//            }
//
//            verticalSlides {
//                // code3 begin
//                for (lines in "[8-12|3-12|2-13|]".toLinePatterns()) {
//                    dslSlide {
//                        autoAnimate = true
//                        slideConfig {
//                            transition = Transition.NONE
//                        }
//                        content {
//                            h2 { +"Animated Code without Line Numbers" }
//                            val file = "src/main/resources/json-example.json"
//                            codeSnippet {
//                                dataId = "code-animation"
//                                language = "json"
//                                highlightPattern = "none"
//                                +include(file, linePattern = lines)
//                            }
//                        }
//                    }
//                }
//                // code3 end
//
//                markdownSlide {
//                    content {
//                        """
//                        ## Slide Definition
//                        ```kotlin []
//                        ${include(slides, beginToken = "code3 begin", endToken = "code3 end")}
//                        ```
//                        <a id="ghsrc" href="${srcrefLink("code3", true)}" target="_blank">GitHub Source</a>
//                        """
//                    }
//                }
//            }
//
//            verticalSlides {
//                // code4 begin
//                for (lines in "[8-12|3-12|2-13|]".toLinePatterns().zip(listOf(3, 3, 2, 1))) {
//                    dslSlide {
//                        autoAnimate = true
//                        slideConfig {
//                            transition = Transition.NONE
//                        }
//                        content {
//                            h2 { +"Animated Code with Line Numbers" }
//                            val file = "src/main/resources/json-example.json"
//                            codeSnippet {
//                                dataId = "code-animation"
//                                language = "json"
//                                lineOffSet = lines.second
//                                +include(file, linePattern = lines.first)
//                            }
//                        }
//                    }
//                }
//                // code4 end
//
//                markdownSlide {
//                    content {
//                        """
//                        ## Slide Definition
//                        ```kotlin []
//                        ${include(slides, beginToken = "code4 begin", endToken = "code4 end")}
//                        ```
//                        <a id="ghsrc" href="${srcrefLink("code4", true)}" target="_blank">GitHub Source</a>
//                        """
//                    }
//                }
//            }
//
//            verticalSlides {
//                // image begin
//                markdownSlide {
//                    // Image size is controlled by css above
//                    content {
//                        """
//                        ## Images
//
//                        ![revealjs-image](images/revealjs.png)
//                        """
//                    }
//                }
//                // image end
//
//                markdownSlide {
//                    content {
//                        """
//                        ## Slide Definition
//                        ```kotlin []
//                        ${include(slides, beginToken = "image begin", endToken = "image end")}
//                        ```
//                        <a id="ghsrc" href="${srcrefLink("image", true)}" target="_blank">GitHub Source</a>
//                        """
//                    }
//                }
//            }
//
//            verticalSlides {
//                // others begin
//                markdownSlide {
//                    id = "otherslides"
//                    content {
//                        """
//                        ## Other Presentations Defined In Slides.kt
//                        <span style="text-align: left; text-indent: 25%;">
//
//                        [🐦 greattalk1/ Slides](/greattalk1)
//
//                        [🐦 greattalk1/other.html Slides](/greattalk1/other.html)
//
//                        [🐦 greattalk2.html Slides](/greattalk2.html)
//                        </span>
//                        """
//                    }
//                }
//                // others end
//
//                markdownSlide {
//                    content {
//                        """
//                        ## Slide Definition
//                        ```kotlin
//                        ${include(slides, beginToken = "others begin", endToken = "others end")}
//                        ```
//                        <a id="ghsrc" href="${srcrefLink("others", true)}" target="_blank">GitHub Source</a>
//                        """
//                    }
//                }
//            }
//        }
//
//        presentation {
//            path = "greattalk1"
//
//            presentationConfig {
//                topRightHref = "/#/otherslides"
//                topRightTitle = "Go back to main presentation"
//                topRightText = "🔙"
//            }
//
//            dslSlide {
//                content {
//                    h2 { +"greattalk1/index.html Slides" }
//                }
//            }
//        }
//
//        presentation {
//            path = "greattalk1/other.html"
//
//            presentationConfig {
//                topRightHref = "/#/otherslides"
//                topRightTitle = "Go back to main presentation"
//                topRightText = "🔙"
//            }
//
//            dslSlide {
//                content {
//                    h2 { +"greattalk1/other.html slides" }
//                }
//            }
//        }
//
//        presentation {
//            path = "greattalk2.html"
//
//            presentationConfig {
//                topRightHref = "/#/otherslides"
//                topRightTitle = "Go back to main presentation"
//                topRightText = "🔙"
//            }
//
//            dslSlide {
//                content {
//                    h2 { +"greattalk2.html slides" }
//                }
//            }
//        }
    }
}
