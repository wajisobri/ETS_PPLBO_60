# ETS PPLBO Praktek
## Simple Food Ordering System on Microservices

Nama 	: Sobri Waskito Aji  
NIM : 201524060  
Kelas : D4TI-3B

## Tech Stack

 - Spring Boot Java Framework
 - MySQL
 - MongoDB
 - RabbitMQ
 
## Prerequisites
 - [x] Erlang
Download: [https://www.erlang.org/downloads](https://www.erlang.org/downloads)
 - [x] RabbitMQ (*as Message Broker*)
 Download: [https://www.rabbitmq.com/download.html](https://www.rabbitmq.com/download.html).
 - [x] Java JDK 17
 - [x] MySQL
 - [x] MongoDB
 - [x] IntelliJ IDE

## API Documentation/Tester (Postman)
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/17008485-5205e1d1-dcbd-47b9-ab9e-25fab5df06b9?action=collection%2Ffork&collection-url=entityId%3D17008485-5205e1d1-dcbd-47b9-ab9e-25fab5df06b9%26entityType%3Dcollection%26workspaceId%3Daca6b7f9-c619-4e69-b65f-936467387cf8)

## Services

**Cafe Service**
 - [x] Menyimpan informasi terkait cafe (misalnya lokasi dan jam operasional) serta menu makanan dan minuman
 - [x] Menyediakan API untuk mengambil informasi café
 - [x] Menyediakan antarmuka administrasi untuk menambah, mengubah, dan menghapus informasi menu
 - [x] Menyediakan antarmuka administrasi untuk mengelola ketersediaan stok bahan makanan pada Kitchen Service

**Menu Service**
 - [x] Menyediakan API untuk mengambil informasi menu dan bahan makanan yang dibutuhkan setiap menu
 - [x] Menyediakan antarmuka administrasi untuk menambah, mengubah, dan menghapus informasi menu
 
**Consumer Service**
 - [x] Menyimpan informasi pelanggan dalam database
 - [x] Menyediakan antarmuka administrasi untuk mengelola informasi pelanggan
 - [x] Menyediakan API untuk mengambil informasi pelanggan

**Order Service**
 - [x] Menerima pesanan dari pelanggan melalui aplikasi, website atau secara langsung di tempat melalui Cashier Service 
 - [x] Memvalidasi pesanan untuk memastikan informasi yang diberikan benar dan lengkap
 - [x] Menyimpan informasi pesanan
 - [ ] Mengirimkan pesanan ke dapur untuk diproses
 - [x] Memantau status pesanan dan memberikan notifikasi ke pelanggan tentang status pesanan mereka

**Payment Service**
 - [ ] Mengintegrasikan aplikasi dengan gateway pembayaran
 - [x] Memproses pembayaran pelanggan
 - [x] Menyimpan informasi pembayaran
 - [x] Menyediakan antarmuka administrasi untuk mengelola transaksi pembayaran

**API Gateway Service**
 - [x] Mengelola interaksi antar endpoint

**Discovery Server (Service Registry) Service**
 - [x] Menjalankan konsep discovery pattern untuk komunikasi proses IPC

**Notification Service**
 - [x] Menerima informasi dari service lain, seperti Order Service, Reservation Service, ketika terjadi perubahan pada pesanan atau reservasi pelanggan. 
 - [x] Mengirimkan notifikasi kepada pelanggan tentang status pesanan, meliputi konfirmasi pemesanan, pembaruan status pesanan, dan pengingat tagihan pembayaran

**UPCOMING SERVICE**
- Cashier Service
- Kitchen Service
- Reservation Service
- Delivery Service
- Delivery Tracking Service
- Cart Service
- Reporting Service

## Business Capabilities and Service Mapping
![Business Capabilities and Service Mapping](../master/other/PPLBO-Nomor%201.drawio.png?raw=true)

## Service Architecture (in progress)
![Service Architecture](../master/other/PPLBO-Nomor%204.drawio.png)
Dalam arsitektur, terdapat dua jenis komunikasi:
1. Synchronous Communication
Komunikasi jenis ini digunakan pada service yang tidak menerapkan "event-driven" design pattern

1. Asynchronous Communication
Komunikasi jenis ini digunakan pada service yang menerapkan "event-driven" design pattern

## Design Pattern used

**Problem**
1. Bagaimana mengimplementasikan transaksi yang menjaga konsistensi data di antara berbagai service?
2. Bagaimana mengimplementasikan query untuk mengambil data dari berbagai service?

**Solution**

Untuk mengatasi hal tersebut, maka digunakan **Data Consistency Pattern**, yakni Event-Driven, Event Sourcing, dan SAGA.

**1. Event-Driven (Messaging)**

Event-Driven untuk menyelesaikan masalah pertama. Dimana setiap event yang terjadi dicatat aktivitasnya sebagai sebuah event. Misalnya, dalam perilaku Order: Order Created, Order Cancelled, dan Order Delivered.

Event-Driven menggunakan service khusus yang dinamakan Message Broker. Message Broker ini akan mengelola event-event berdasarkan kebutuhan service lain yang melakukan publish atau subscribe terhadap sumber penghasil event yang dituju. Ketika sebuah service mem-publish event melalui message broker, maka semua event yang men-subsribe akan mendapatkan informasi terkait event tersebut dan kemudian menyinkrokannya. Semua hal tersebut dilakukan secara asynchronous sehingga tidak akan menghambat performance dari aplikasi.

Contohnya, ketika terdapat pesanan baru melalui Order Service, maka Order Service akan mem-publish event tersebut ke message broker. Kemudian, message broker yang bertugas memdistribusikan event tersebut ke service yang membutuhkan, misalnya Customer Service, Payment Service, dan Notification Service.

Dalam program ini, Event-Driven diimplementasi pada interaksi secara asynchronous antara service _order_ dan _payment_. Untul lebih detailnya terdapat pada file berikut:
1. [Event-Driven dalam Order Service](https://github.com/wajisobri/ETS_PPLBO_60/tree/master/order-service/src/main/java/com/programming/wajisobri/orderservice/service) : `placeOrder()` dan `cancelOrder()` akan menghasilkan dan mengirim event message order ke RabbitMQ. Kemudian, di-consume oleh service yang membutuhkan (payment)
2. [Event-Driven dalam Payment Service](https://github.com/wajisobri/ETS_PPLBO_60/tree/master/payment-service/src/main/java/com/programming/wajisobri/paymentservice/service) : if `processPayment()` dan `payInvoice()`  akan menghasilkan dan mengirim event message payment ke RabbitMQ. Kemudian, di-consume oleh service yang membutuhkan (order)
3. [Event-Driven dalam Notification Service](https://github.com/wajisobri/ETS_PPLBO_60/tree/master/payment-service/src/main/java/com/programming/wajisobri/notificationservice/service) : listen for `placeOrder()` and trigger `pushNotification()`

Tujuannya, agar komunikasi antar service dapat terjadi secara asynchronous untuk meningkatkan performa. Sehingga service _order_ tidak perlu menunggu proses pada _payment_ selesai untuk melanjutkan proses lain.

**2. Event Sourcing**

Penggunaan event-sourcing digunakan untuk menyimpan event-sourced aggregate sebagai urutan event. Setiap event mepresentasikan perubahan state. State terkini dapat diketahui dengan me-replay event tersebut.

Berdasarkan hal tersebut, event sourcing dapat memantau histori aggregate sehingga memudahkan auditing. Namun, perlu menggunakan CQRS (Command Query Responsibility Segragation) sebagai pattern untuk melakukan query pada event yang disimpan. Selain itu, akan dilakukan pengambilan dengan Snapshot dalam jangka waktu tertentu untuk mempercepat performa

Dalam program ini, Event Sourcing diimplementasi pada interaksi secara asynchronous antara service _order_ dan _payment_. Untul lebih detailnya terdapat pada file berikut:
1. [Event Sourcing dalam Order Service](https://github.com/wajisobri/ETS_PPLBO_60/tree/master/order-service/src/main/java/com/programming/wajisobri/orderservice/service) : `placeOrder()` dan `cancelOrder()` yang menerapkan event-driven pattern, akan menyimpan event di dalam Event Store dan di-consume oleh service yang membutuhkan (payment) jika terjadi kegagalan sistem
2. [Event Sourcing dalam Payment Service](https://github.com/wajisobri/ETS_PPLBO_60/tree/master/payment-service/src/main/java/com/programming/wajisobri/paymentservice/service) : if `processPayment()` dan `payInvoice()` yang menerapkan event-driven pattern, akan menyimpan event di dalam Event Store dan di-consume oleh service yang membutuhkan (order) jika terjadi kegagalan sistem

Tujuannya, agar setiap perubahan state dalam program dicatat oleh sistem agar nantinya dapat mempermudah proses auditing dan sebagainya.

**3. SAGA**

SAGA memastikan konsistensi data dalam mengelola proses local transaction yang terjadi di masing-masing service, yang dapat berupa memperbarui database atau mem-publish informasi ke service selanjutnya. Jika transaksi gagal di salah satu service, maka SAGA akan menjalankan rangkaian mekanisme “revert/compensating” yang membatalkan perubahan sebelumnya pada commit oleh local transaction service sebelumnya.

Terdapat dua pendekatan, tetapi yang akan digunakan adalah pendekatan choreography, dimana tidak ada koordinator, service menjalankan SAGA dengan bekerja sama (tahu apa dan kapan harus dilakukan), tanpa ada yang mengendalikan

Dalam program ini, SAGA diimplementasi pada interaksi antara service _order_, _payment_, dan _cafe_. Untuk lebih detailnya terdapat pada file berikut:
1. [SAGA dalam Order Service](https://github.com/wajisobri/ETS_PPLBO_60/tree/master/order-service/src/main/java/com/programming/wajisobri/orderservice/service) : `cancelOrder()` trigger `restoreIngredient()` 

2. [SAGA dalam Payment Service](https://github.com/wajisobri/ETS_PPLBO_60/tree/master/payment-service/src/main/java/com/programming/wajisobri/paymentservice/service) : if `receivePaymentRequest()` fail, trigger `cancelOrder()`

Tujuannya agar ketika terjadi kegagalan saat proses pembayaran, maka order akan dicancel dan bahan makanan (ingredient) yang telah dikurang akan dikembalikan.
