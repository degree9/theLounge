FROM adzerk/boot-clj:latest

ENV BOOT_VERSION=2.4.2

RUN /usr/bin/boot web -s doesnt/exist repl -e '(System/exit 0)' && rm -rf target

ENTRYPOINT ["/usr/bin/boot", "repl"]
