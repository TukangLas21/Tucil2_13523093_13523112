# Kompresi Gambar dengan Metode Quadtree - Tugas Kecil 2 IF2211 Strategi Algoritma
<img src="demo.gif" alt="demo animation" width="350">

## Deskripsi Singkat
Kompresi gambar merupakan suatu proses untuk mengurangi ukuran suatu file gambar tanpa mengurangi kualitas citranya secara signifikan. Salah satu cara yang dapat digunakan untuk melakukan kompresi gambar adalah dengan memanfaatkan struktur data Quadtree. Quadtree sendiri merupakan sebuah struktur data pohon atau _tree_ yang setiap simpulnya memiliki 4 simpul anak. Quadtree dapat digunakan untuk menyederhanakan piksel-piksel pada citra menjadi warna yang sama sehingga mengurangi jumlah data yang diperlukan. <br>
Algoritma program ini memanfaatkan pendekatan _divide and conquer_. Pendekatan ini dilakukan dengan membagi suatu blok citra menjadi 4 sub-blok, kemudian mengevaluasi eror keempat sub-blok tesebut. Jika eror suatu sub-blok melebihi ambang batas, sub-blok tersebut akan membagi menjadi 4 sub-sub-blok dan prosesnya berulang. Proses ini dilakukan secara rekursif hingga eror blok berada di atas ambang batas yang ditentukan atau ukuran blok setelah dibagi empat menjadi kurang dari ukuran blok minimum yang ditentukan. Pada program ini, terdapat empat metode pengukuran eror, yakni dengan variansi, _mean absolute difference_, _max pixel difference_, dan entropi.

## Persyaratan Sistem
Sebelum menjalankan program, pastikan Anda telah menginstalasi Java Development Kit (JDK) untuk menjalankan program berbasis Java ini. Anda dapat mengunduh JDK melalui [pranala](https://www.oracle.com/in/java/technologies/downloads/#java23) ini.

## Instalasi / Memulai
Silakan clone repositori ini dengan menjalankan perintah di bawah pada terminal.
```sh
git clone https://github.com/TukangLas21/Tucil2_13523093_13523112.git
cd Tucil2_13523093_13523112
```

### Menjalankan Program
Program perlu dikompilasi dulu sebelum dijalankan. Silakan menjalankan perintah di bawah ini untuk mengompilasi dan menjalankan program. Pastikan Anda sudah berada di folder Tucil2_13523093_13523112.
```sh
javac -d bin src/quadtree/*.java src/Main.java
java -cp bin Main
```
Setelah program berjalan, masukkan input sesuai dengan yang diminta program.
Input yang diminta meliputi:
1. Alamat absolute dari file gambar yang ingin dikompresi
2. Indeks metode perhitungan error yang ingin digunakan:
    - 1. Variansi
    - 2. Mean Absolute Difference
    - 3. Max Pixel Difference
    - 4. Entropi
3. Threshold error
4. Ukuran minimum blok
5. Target kompresi (0 untuk menonaktifkan)
6. Alamat folder untuk menyimpan gambar hasil kompresi
7. Nama file gambar hasil kompresi
8. Apakah ingin membuat GIF (Y/N)
9. Alamat folder untuk menyimpan GIF (opsional)
10. Nama file GIF (opsional)
11. Batas atas pencarian threshold untuk target kompresi (opsional)

## Laporan
[Google Docs Laporan](https://docs.google.com/document/d/1DCzy2bY037Ounp9AMHOxyg9fs--8FGSJflN1tjw2ET8/edit?usp=sharing)

## Berkontribusi
Silakan fork repositori ini jika Anda ingin melakukan perubahan apapun. 

## Authors
- Karol Yangqian Poetracahya / 13523093 / K02
- Aria Judhistira / 13523112 / K02
