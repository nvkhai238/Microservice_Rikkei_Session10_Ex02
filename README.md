# Session 10 — Exercise 02: Producer gửi sự kiện đơn hàng thuốc

Pharmacy-Service nhận lệnh thanh toán tại `POST /api/orders`, đóng gói `OrderEvent` thành JSON và chờ Kafka xác nhận. Message key là `medicineId`, vì vậy các đơn của cùng loại thuốc được Kafka định tuyến vào cùng partition.

## Chạy

```bash
docker compose up -d
gradle :pharmacy-service:bootRun
```

Gửi đơn thử:

```bash
curl -i -X POST http://localhost:8090/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"medicineId":"paracetamol","quantity":2}'
```

Phản hồi gồm `orderId`, topic, partition và offset sau khi broker xác nhận. Kiểm tra dữ liệu bằng:

```bash
docker compose exec kafka /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:29092 --topic medicine-stock-events --from-beginning \
  --property print.key=true --property key.separator=:
```

`docker compose down` dừng hạ tầng; `docker compose down -v` cũng xóa dữ liệu Kafka.
