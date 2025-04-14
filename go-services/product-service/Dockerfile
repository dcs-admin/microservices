# FROM golang:1.24 AS builder
# WORKDIR /app
# COPY . .
# RUN go build -o main .

# FROM debian:bookworm-slim
# WORKDIR /app
# COPY --from=builder /app/main .
# CMD ["./main"]

# --
# Build Stage
FROM golang:1.24 AS builder

WORKDIR /app
COPY go.mod ./
COPY go.sum ./

RUN go mod download

COPY . .
RUN go build -o main .

# Run Stage
FROM debian:bookworm-slim

WORKDIR /app
COPY --from=builder /app/main .
COPY config.env ./

CMD ["./main"]
