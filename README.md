[![](https://github.com/wutsi/wutsi-qr-server/actions/workflows/master.yml/badge.svg)](https://github.com/wutsi/wutsi-qr-server/actions/workflows/master.yml)

[![JDK](https://img.shields.io/badge/jdk-11-brightgreen.svg)](https://jdk.java.net/11/)
[![](https://img.shields.io/badge/maven-3.6-brightgreen.svg)](https://maven.apache.org/download.cgi)
![](https://img.shields.io/badge/language-kotlin-blue.svg)

API for generating QR Code

# QR Code Format

The format for QR Code is:

```xml

<entity>:
    <id>:
        <expires>
```

Where:

- `<entity>`: The type of entity encoded. Values are:
    - `url`
    - `account`
    - `product`
    - `order`
    - `payment_request`
- `<id>`: The identifier of the entity
- `<expires>`: The time in seconds when this code expires. For static QR code, the expiry date is set in 100 years :-)

# Links

- [API](https://wutsi.github.io/wutsi-qr-server/api/)
- [Documentation](docs/)
