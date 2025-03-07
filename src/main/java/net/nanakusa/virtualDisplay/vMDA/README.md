# V-MDA
V-MDA is a virtual monochrome display adaptor.
It is intend to work as 80 times 25 character display. 

## Usage
`MDA_IO`クラスのコンストラクタは以下のようになっています. 

```java
public MDA_IO(SubMemory memory);
```

`SubMemory`を渡すと, `SubMemory`内にディスプレイに表示する文字列を保持するための領域を確保します. 
`SubMemory`のサイズが`25 * 80` 以下である場合, 初期化に失敗します. 

`Submemory.write`を通じて255までの値を書き込むと, ASCIIコードに従ってアルファベットを表示します. 

