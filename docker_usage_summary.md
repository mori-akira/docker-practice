# docker-compose 環境構築実践
次の方針でdocker-composeによる環境構築を実践する。
1. practice1 - 公式introductionを使用して実践
2. practice2 - Spring boot Runnerアプリを作成し、コンテナ化・ホストとの接続を確認
3. practice3 - practice2に加えて、Spring boot MVCとMySQLをコンテナ化し、サンプルAPIを作成

## practice1 - 公式introductionを使用して実践

### 参考
次の公式リファレンスを参考にする。(というより、リファレンスの内容を転載してるだけ)
> https://docs.docker.jp/compose/gettingstarted.html

### 概要
次の構成でファイルを作成する。
#### ディレクトリ構成
<pre>
practice1 ┳ app.py
          ┣ requirements.txt
          ┣ Dockerfile
          ┗ docker-compose.yml
</pre>

#### app.py
処理をpythonで記述する。
Flask・Redisモジュールで、APIサーバーの機能を実現する。
<pre>
from flask import Flask
from redis import Redis

app = Flask(__name__)
redis = Redis(host='redis', port=6379)

@app.route('/')
def hello():
    count = redis.incr('hits')
    return 'Hello world! I have been seen {} times.\n'.format(count)

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
</pre>

#### requirements.txt
app.pyでインストールが必要なモジュールを定義する。
<pre>
flask
redis
</pre>

#### Dockerfile
Dockerイメージを構築する。
<pre>
FROM python:3.4-alpine
ADD . /code
WORKDIR /code
RUN pip install -r requirements.txt
CMD ["python", "app.py"]
</pre>
これはDockerに対して、次の指示を行うものである。
1. Python 3.4イメージを使用して、イメージを構築する。
2. カレントディレクトリ`.`を用いて、イメージ内のパス`/code`に加える。
3. 作業用ディレクトリを`/code`に指定する。
4. Pythonの依存パッケージをインストールする。
5. コンテナに対するデフォルトのコマンドを`python app.py`にする

#### docker-compose.yml
docker-composeで作成するサービスを定義する。
<pre>
version: "3"
services:
  web:
    build: .
    ports:
    - "5000:5000"
  redis:
    image: "redis:alpine"
</pre>
これにより、`web`と`redis`という2つのサービスを定義する。
`web`サービスは、次のように設定する。
* カレントディレクトリにある`Dockerfile`から構築されるイメージを利用する。
* コンテナ公開用のポート5000を、ホストマシンのポート5000にフォーワードする。
Flaskウェブ・サーバーに対するデフォルトポート5000をそのまま使用する。

`redis`サービスは、Docker Hubレジストリから取得したイメージを利用する。

### Composeによるアプリケーションの構築と実行
1. プロジェクト用のディレクトリで`docker-compose up`を実行し、アプリケーションを起動する。
2. http://localhost:5000 にアクセスする。
3. 別のターミナルを起動し、プロジェクト用のディレクトリで`docker-compose down`を実行し、アプリケーションを停止する。
または元のターミナルでCtrl+Cなど中断シグナルを送信する。

### Composeファイルにバインドマウントを追加する
`docker-compose.yaml`を編集して、`web`サービスへのバインドマウントを追加する。
<pre>
version: "3"
services:
  web:
    build: .
    ports:
     - "5000:5000"
    volumes:
     - .:/code
  redis:
    image: "redis:alpine"
</pre>
新しい`<b>volumes</b>`というキーは、ホスト上のカレントディレクトリをコンテナ内の`/code`ディレクトリにマウントする。
これにより、イメージを再構築せずとも実行中のコードを修正可能となる。

### Composeによるアプリケーションの再構築と実行
<cd>docker-compose up</cd>を使用すると、Composeファイルの更新を自動で検出し、アプリは再構築されて実行される。

### その他のコマンド
* `docker-compose up -d`<br>
upコマンドにdオプションを付与すると、デタッチモードで起動する。
* `docker-compose ps`<br>
カレントディレクトリ上で動いているサービスの一覧を確認できる。
* `docker-compose run`<br>
サービスに対してコマンド実行ができる。
例えば、`Web`サービス上でどのような環境変数が定義されているか確認する場合、次のコマンドを使用すればよい。
<pre>
docker-compose run web env
</pre>
* `docker-compose --help`<br>
その他のコマンドを確認できる。
* `docker-compose stop`<br>
up -dによって起動したサービスを停止するコマンドである。
* `docker-compose down`<br>
コンテナも完全に削除し、すべてを終了するコマンドである。

### Tips

#### redisとは
Redisとは、オープンソースの非リレーショナルデータベースのこと。
Key-Valueでデータを保存し、複雑の種類のデータも扱える。
また、コンピュータのメインメモリ上でデータを管理するため、高速なアクセスが可能である。

#### run、up、build、createの違い
|コマンド|処理内容|備考|
|:--:|:---|:---|
|run|イメージ&コンテナ作成&起動|1つのサービスのみ。(1つのサービスに対して1つのコマンドを実行する)|
|up|イメージ&コンテナ作成&起動|depends_onやlinkで紐づいているコンテナもまとめて起動。サービスを一括でまとめて起動したい場合に用いる。|
|build|イメージの作成|-|
|create|コンテナの作成|-|

#### Dockerfileの書き方
公式リファレンス:
> https://docs.docker.jp/engine/reference/builder.html

## practice2 -  Spring boot Runnerアプリを作成し、コンテナ化・ホストとの接続を確認

###ソース
> https://github.com/mori-akira/docker-practice/tree/main/practice2

### 概要
次の構成でファイルを作成する。
#### ディレクトリ構成
<pre>
practice1 ┳ src/
          ┣ jar/
          ┣ Dockerfile
          ┣ docker-compose.yml
          ┗ pom.xml
</pre>

#### src/
Spring bootで処理を記述する。

#### jar/
処理をjarにパッケージしたものを格納する。
今回は`docker_practice2-0.0.1-SNAPSHOT.jar`が格納されている。

#### Dockerfile
Dockerイメージを構築する。
<pre>
FROM openjdk:11
ADD ./jar /code
WORKDIR /code
</pre>
* 今回は、`openjdk:11`イメージをレジストリから取得し、イメージを構築する。
* jarディレクトリ下をイメージ内の`/code`ディレクトリに配置する。

#### docker-compose.yml
サービスを定義する。
<pre>
version: "3"
services:
  runner:
    build: .
    volumes:
     - C:\dev\docker_sample:/docker_sample
</pre>
* ローカルの作業ディレクトリ`C:\dev\docker_sample`を`/docker_sample`にマウントする。

#### pom.xml
mavenプロジェクトを設定する。

### サービスの起動と実行
次のコマンドで実行可能である。
<pre>
docker-compose run runner java -jar docker_practice2-0.0.1-SNAPSHOT.jar "src=/dev/docker_sample/practice2/data" "generateNum=20"
</pre>