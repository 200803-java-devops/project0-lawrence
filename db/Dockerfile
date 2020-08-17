FROM postgres
ENV POSTGRES_USER blackjackdb
ENV POSTGRES_PASSWORD ABC123
ADD *.sql /docker-entrypoint-initdb.d/
EXPOSE 5432