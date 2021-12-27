[![](https://github.com/wutsi/wutsi-qr-server/actions/workflows/master.yml/badge.svg)](https://github.com/wutsi/wutsi-qr-server/actions/workflows/master.yml)

[![JDK](https://img.shields.io/badge/jdk-11-brightgreen.svg)](https://jdk.java.net/11/)
[![](https://img.shields.io/badge/maven-3.6-brightgreen.svg)](https://maven.apache.org/download.cgi)
![](https://img.shields.io/badge/language-kotlin-blue.svg)

API for generating QR Code tokens.&#10;&#10;This API uses the [JWT](https://www.jwt.io) format for representing tokens.&#10;&#10;It&#39;s a string in the format &#96;xxxx.yyyy.zzzz&#96;, componsed of 3 parts:&#10;- The header: This is the &#96;xxxx&#96; part. It&#39;s a based 64 representation of the JSON containing the information about encryption algorigtm.&#10;- The payload: This is the &#96;yyyy&#96; part. The payload contains the representation of the entity encoded.&#10;- The signature: This is the &#96;zzzz&#96; part. The digital signature of the token, to verify the authenticity of the token&#10;

# Installation Prerequisites
## Database Setup
- Install postgres
- Create account with username/password: `postgres`/`postgres`
- Create a database named `wutsi-qr`

## Configure Github
- Generate a Github token for accessing packages from GibHub
  - Goto [https://github.com/settings/tokens](https://github.com/settings/tokens)
  - Click on `Generate New Token`
  - Give a value to your token
  - Select the permissions `read:packages`
  - Generate the token
- Set your GitHub environment variables on your machine:
  - `GITHUB_TOKEN = your-token-value`
  - `GITHUB_USER = your-github-user-name`

## Maven Setup
- Download Instance [Maven 3.6+](https://maven.apache.org/download.cgi)
- Add into `~/m2/settings.xml`
```
    <settings>
        ...
        <servers>
            ...
            <server>
              <id>github</id>
              <username>${env.GITHUB_USER}</username>
              <password>${env.GITHUB_TOKEN}</password>
            </server>
        </servers>
    </settings>
```

## Usage
- Install
```
$ git clone git@github.com:wutsi/wutsi-qr-server.git
```

- Build
```
$ cd wutsi-qr-server
$ mvn clean install
```

- Launch the API
```
$ mvn spring-boot:run
```

That's it... the API is up and running! Start sending requests :-)

# Links
- [API](https://wutsi.github.io/wutsi-qr-server/api/)
- [Documentation](docs/)
