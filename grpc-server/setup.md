* init npm:
```shell
npm init -y
```

* add into main json object in file package.json next:
```json
{
"dependencies": {
 "@grpc/proto-loader": "0.5.0",
 "async": "1.5.2",
 "google-protobuf": "3.0.0",
 "@grpc/grpc-js": "1.1.0",
 "lodash": "4.6.1",
 "minimist": "1.2.0"
 }
}
```

* build server
```shell
npm install
```

* run server
```shell
node index.js
```