FROM ubuntu:22.04

SHELL ["/bin/bash", "-c"]

#対話モード無効化(tzdata用)
ENV DEBIAN_FRONTEND=noninteractive

# 必要なパッケージを root でインストール
RUN apt-get update && \
    apt-get install -y curl zip unzip sudo tar git locales tzdata && \
    locale-gen ja_JP.UTF-8 && \
    update-locale LANG=ja_JP.UTF-8 && \
    ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime && \
    dpkg-reconfigure --frontend noninteractive tzdata

# 一般ユーザー（vscode）作成
RUN groupadd -g 1000 workspace && \
useradd -m -s /bin/bash -u 1000 -g workspace vscode && \
echo "vscode ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/workspace && \
chmod 0440 /etc/sudoers.d/workspace

# vscodeユーザーに切り替え
USER vscode

# SDKMANのインストール + Java
RUN curl -s "https://get.sdkman.io" | bash && \
bash -c "source /home/vscode/.sdkman/bin/sdkman-init.sh && \
sdk install java"

# 環境変数の設定
ENV SDKMAN_DIR="/home/vscode/.sdkman"
ENV JAVA_HOME="/home/vscode/.sdkman/candidates/java/current"
ENV PATH="${JAVA_HOME}/bin:${PATH}"
ENV LANG=ja_JP.UTF-8
ENV LC_ALL=ja_JP.UTF-8
ENV TZ=Asia/Tokyo