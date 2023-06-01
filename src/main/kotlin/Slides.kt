import com.kslides.*
import com.pambrose.srcref.Api.srcrefUrl
import kotlinx.css.h3
import kotlinx.css.img
import kotlinx.html.*

fun main() {
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
                            a {
                                href = "/database_partitioning"
                                +"Partitioning"
                            }
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
                        - 資料庫高可用性，避免單點故障
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### Replication 缺點

                        - 磁碟空間增長
                        - 需維護資料庫 Cluster，系統複雜度上升
                            - 高可用性導致維運成本上升
                            - 集群腦裂
                            - Transaction 管理
                            - Flush DB Log，寫入性能降低
                        - 資料同步延遲及不一致性
                            - Replication Lag，造成讀寫一致性
                            - Concurrent Write
                        """
                    }
                }

                dslSlide {
                    content {
                        h3 { +"Read Your Write (讀寫一致性)" }
                        img {
                            src = "images/database/database-replication-read-your-write-2.png"; classes =
                                setOf("image75")
                        }
                        ul {
                            li {
                                +"寫入至 Master"
                            }
                            li {
                                +"立即至 Slave 讀取，取不到值"
                            }
                        }
                    }
                }

                dslSlide {
                    content {
                        h3 { +"Monotonic Read (資料倒退)" }
                        img {
                            src = "images/database/database-replication-monotonic-read.png"; classes =
                                setOf("image75")
                        }
                        ul {
                            li {
                                +"連續讀取，讀到新資料後，接著又讀到舊資料"
                            }
                            li {
                                +"會發生於不知是從 Master 還是 Slave 中讀取資料"
                            }
                        }
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### 如何解決讀寫一致性
                        
                        - Master Pinning： 
                          - 僅從 Master 讀取
                        - Fragmented Pinning： 
                          - 寫入後短時間內，固定至 Master
                        - Master Fallback： 
                          - Slave 無法讀取時，切換至 Master
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### 如何達到讀寫分離
                        
                        - Native： 透過程式碼實作，直接指定讀取資料庫
                        - Client Dependency： 透過第三方套件，達到功能
                        - Infra Middleware： 透過外部 Infra，達到功能
                          - Apache ShardingSphere
                          - Vitess: YouTube, Google
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### Spring Boot Demo
                        
                        - [GitHub Source](https://github.com/Vipcube/Demo-SpringBoot-DB-ReadWriteSplitting)
                        - 實作採用 JPA 
                        - Native 實現讀寫分離
                        - Apache ShardingSphere 實現讀寫分離
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### Native 實現讀寫分離
                        """
                    }
                }

                markdownSlide {
                    val src = "src/main/resources/application.yml"
                    content {
                        """
                        ### Native YAML Config
                        ```yaml
                        ${include(githubRawUrl("Vipcube", "Demo-SpringBoot-DB-ReadWriteSplitting", src), "[31-40]")}
                        ```
                        """
                    }
                }

                markdownSlide {
                    val src = "src/main/java/org/vipcube/spring/demo/config/DataSourceConfig.java"
                    content {
                        """
                        ### Native Master DataSource
                        ```java [1|2|3,9]
                        ${include(githubRawUrl("Vipcube", "Demo-SpringBoot-DB-ReadWriteSplitting", src), "[43-51]")}
                        ```
                        """
                    }
                }

                markdownSlide {
                    val src = "src/main/java/org/vipcube/spring/demo/config/DataSourceConfig.java"
                    content {
                        """
                        ### Native Slave DataSource
                        ```java [1|2|3,9]
                        ${include(githubRawUrl("Vipcube", "Demo-SpringBoot-DB-ReadWriteSplitting", src), "[56-63]")}
                        ```
                        """
                    }
                }

                markdownSlide {
                    val src = "src/main/java/org/vipcube/spring/demo/config/DataSourceConfig.java"
                    content {
                        """
                        ### Native Dynamic DataSource
                        ```java [4,5|10|16,17]
                        ${include(githubRawUrl("Vipcube", "Demo-SpringBoot-DB-ReadWriteSplitting", src), "[65-83]")}
                        ```
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
                    val src = "src/main/resources/application.yml"
                    content {
                        """
                        ### ShardingSphere YAML Config
                        ```yaml
                        ${include(githubRawUrl("Vipcube", "Demo-SpringBoot-DB-ReadWriteSplitting", src), "[52-56]")}
                        ```
                        """
                    }
                }

                dslSlide {
                    content {
                        h3 { +"Live Demo" }
                    }
                }
            }
        }

        presentation {
            path = "database_partitioning"

            presentationConfig {
                topRightHref = "/database_performance"
                topRightTitle = "Go back to database performance page"
                topRightText = "🔙"
            }

            verticalSlides {
                markdownSlide {
                    content {
                        """
                        ### Database Partitioning

                        - Horizontal Partitioning: Sharding
                        - Vertical Partitioning: Increase Service
                        """
                    }
                }

                markdownSlide {
                    content {
                        """
                        ### Database Partitioning

                        - Horizontal Partitioning: Sharding
                        - Vertical Partitioning: Increase Service
                        """
                    }
                }
            }
        }
    }
}
