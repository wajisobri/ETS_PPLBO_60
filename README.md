# ETS PPLBO Praktek
## Food Ordering System on Microservices

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

## API Tester (Postman)
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/17008485-5205e1d1-dcbd-47b9-ab9e-25fab5df06b9?action=collection%2Ffork&collection-url=entityId%3D17008485-5205e1d1-dcbd-47b9-ab9e-25fab5df06b9%26entityType%3Dcollection%26workspaceId%3Daca6b7f9-c619-4e69-b65f-936467387cf8)

## Services

**Cafe Service**
 - [x] Menyimpan informasi terkait cafe (misalnya lokasi dan jam operasional) serta menu makanan dan minuman
 - [x] Menyediakan API untuk mengambil informasi café
 - [ ] Menyediakan UI administrasi untuk menambah, mengubah, dan menghapus informasi menu
 - [ ] Menyediakan UI administrasi untuk mengelola ketersediaan stok bahan makanan pada Kitchen Service

**Menu Service**
 - [x] Menyediakan API untuk mengambil informasi menu dan bahan makanan yang dibutuhkan setiap menu
 - [ ] Menyediakan UI administrasi untuk menambah, mengubah, dan menghapus informasi menu
 
**Consumer Service**
 - [x] Menyimpan informasi pelanggan dalam database
 - [ ] Menyediakan UI administrasi untuk mengelola informasi pelanggan
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
 - [ ] Menyediakan antarmuka administrasi untuk mengelola transaksi pembayaran

**API Gateway Service**
 - [x] Mengelola interaksi antar endpoint

**Discovery Server Service**
 - [x] Menjalankan konsep discovery pattern untuk komunikasi proses IPC

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

**Event-Driven (Messaging)**
Event-Driven untuk menyelesaikan masalah pertama. Dimana setiap event yang terjadi dicatat aktivitasnya sebagai sebuah event. Misalnya, dalam perilaku Order: Order Created, Order Cancelled, dan Order Delivered.

Event-Driven menggunakan service khusus yang dinamakan Message Broker. Message Broker ini akan mengelola event-event berdasarkan kebutuhan service lain yang melakukan publish atau subscribe terhadap sumber penghasil event yang dituju. Ketika sebuah service mem-publish event melalui message broker, maka semua event yang men-subsribe akan mendapatkan informasi terkait event tersebut dan kemudian menyinkrokannya. Semua hal tersebut dilakukan secara asynchronous sehingga tidak akan menghambat performance dari aplikasi.

Contohnya, ketika terdapat pesanan baru melalui Order Service, maka Order Service akan mem-publish event tersebut ke message broker. Kemudian, message broker yang bertugas memdistribusikan event tersebut ke service yang membutuhkan, misalnya Customer Service, Payment Service, dan Notification Service.

**Event Sourcing**
Penggunaan event-sourcing digunakan untuk menyimpan event-sourced aggregate sebagai urutan event. Setiap event mepresentasikan perubahan state. State terkini dapat diketahui dengan me-replay event tersebut.

Berdasarkan hal tersebut, event sourcing dapat memantau histori aggregate sehingga memudahkan auditing. Namun, perlu menggunakan CQRS (Command Query Responsibility Segragation) sebagai pattern untuk melakukan query pada event yang disimpan. Selain itu, akan dilakukan pengambilan dengan Snapshot dalam jangka waktu tertentu untuk mempercepat performa
