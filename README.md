# JPA

### 準備
以下を終わらせておいてください  

### 本日発表すること

* JavaEEとは
* JPAとは
* JPAのハンズオン
    * 単純なエンティティの作成
    * CRUD操作(永続化コンテキストにも触れる)
    * リレーション
    * カスケードタイプ
    * Fetchタイプ
    * JPQL
        * Netbeansだと補完してくれる
        * 名前付きクエリ
    * CriteriaAPI

### JavaEEとは
Javaは当初、デスクトップやブラウザ上で動作するアプリケーションで利用されていました  
そこでサーバーサイドで開発するためのフレームワークJ2EE 1.2を作成しました  
J2EE 1.2には、JSPやServletが含まれていたそうです  
それから1.3、1.4とリリースが進み、その次のバージョンアップではJava Platform Enterprise Edition(Java EE)と改名されたそうです  
JavaEEは企業システムの開発に必要な機能を一つにまとめた仕様の総称です  
例えば以下の機能に関する仕様が含まれています  

**Java Persistence API(JPA)**

ORマッパー  
今回の勉強会のメインとなります

**Java Message Service(JMS)**

外部のメッセージングサービスと非同期で通信が行える  

**Servlet**

Tomcatやjettyといったサーブレットコンテナ上で動作し、Httpリクエストに対してプログラムを実行しレスポンスを返します  
JavaEEにはJSFというフレームワークがありますが、裏ではServletが動いています  

### JPAとは
Java EEに含まれているORMです  
Javaのオブジェクトにアノテーションを付けることで、DBの値をマッピングしてくれます  
JPAにはキャッシュ機能が含まれており、DBへの負荷を減らしてくれます  

JPAには以下の主要な構成要素があります  

**エンティティ**

```java

```

**エンティティマネージャ**

```java

```

**クエリ**

```java

```

**永続化ユニット**

```xml

```

JPAは単なる仕様であり、インタフェースが定義されているだけで有名な以下二つの実装があります  

* eclipse link
* hibernate

基本的にJPA標準のAPIを利用していれば両者は置き換えが可能だそうです  
また、それぞれ独自の追加機能も実装されています  

**マルチテナント(EclipseLink/Hibernate)**

```
HashMap properties = new HashMap();
properties.put("tenant.id", "707");
...     
EntityManager em = Persistence.createEntityManagerFactory("multi-tenant", 
      properties).createEntityManager();
```

**データベース変更通知のハンドリング(EclipseLink)**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://java.sun.com/xml/ns/persistence persistence_2_0.xsd"
                version="2.0">
    <persistence-unit name="acme" transaction-type="RESOURCE_LOCAL">
        <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
        <class>model.Order</class>
        <class>model.OrderLine</class>
        <class>model.Customer</class>
        <exclude-unlisted-classes>false</exclude-unlisted-classes>
        <properties>
            <property name="eclipselink.cache.database-event-listener" value="DCN"/>
        </properties>
    </persistence-unit>
</persistence>
```

**自然キーのサポート(Hibernate)**

```java
@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = “id”, updatable = false, nullable = false)
  private Long id;

  @NaturalId
  private String isbn;
}
```

**主キーによる複数エンティティ取得のサポート(Hibernate)**

```java
Session session = em.unwrap(Session.class);
MultiIdentifierLoadAccess<PersonEntity> multiLoadAccess = session.byMultipleIds(PersonEntity.class);
List<PersonEntity> persons = multiLoadAccess.multiLoad(1L, 2L, 3L);
```


**作成日時・更新日時の管理(Hibernate)**

```java
@Entity
public class MyEntity {
 
    @Id
    @GeneratedValue
    private Long id;
 
    @CreationTimestamp
    private LocalDateTime createDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updateDateTime;
```

**関連付けられていないテーブルの結合(Hibernate)**

```java
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
	
List<Object[]> results = em.createQuery("SELECT p.firstName, p.lastName, n.phoneNumber FROM Person p LEFT JOIN PhoneBookEntry n ON p.firstName = n.firstName AND p.lastName = n.lastName").getResultList();
```

### JPAハンズオン
ここからはJPAのハンズオンに入っていきます  
多くの場合はJavaEEサーバー上で動かすと思うのですが、今回は単純化のためにJavaSE上で動かします  
なので今回Tomcat等のサーバーは不要です  

