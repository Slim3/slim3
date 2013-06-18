# slim3 snapshot repository

## pom.xml

```xml
<project>
  <repositories>
    <repository>
      <id>slim3-snapshot-repo</id>
      <url>http://slim3.github.com/slim3/</url>
      <releases>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
      </releases>
      <snapshots>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
      </snapshots>
    </repository>
  </repositories>
  <pluginRepositories>
    <pluginRepository>
      <id>slim3-snapshot-repo</id>
      <url>http://slim3.github.com/slim3/</url>
      <releases>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
      </releases>
      <snapshots>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
      </snapshots>
    </pluginRepository>
  </pluginRepositories>
  <dependencies>
    <dependency>
      <groupId>org.slim3</groupId>
      <artifactId>slim3</artifactId>
      <version>1.0.17-SNAPSHOT</version>
    </dependency>
  </dependencies>
</project>

```
