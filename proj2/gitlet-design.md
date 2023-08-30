# Gitlet Design Document

**Name**:Tonghui Wang

## Classes and Data Structures

### Commit class 提交文件

#### Instances

*  `String Message`记录提交中用户输入的信息
* `Data Timestamp`时间戳，初始提交中为`timestamp=Data(0)`
* `List<String> Parent`父提交，是一个字符串列表，保存其每一个父提交的SHA1值
* `Map<String String> Blobs` 用一个TreeMap保存提交中文件名和Blob的对应关系。 文件名->Blob
* `String id`保存这个Commit的SHA1值作为文件名和id，具体计算方式是sha1(message, timestamp, parent, blobs)


### StagingArea暂存区
#### Instances
* added - 暂存区中需要被保存的文件  HashMap(String, String) 文件名->Blob
* removed - 暂存区中需要被移除的文件 文件名 HashSet(String)


### Blob 存储文件内容
**当一个文件被提交到暂存区后，根据这个文件的名字和内容生成一个blob文件，blob文件存储在.gitlet/Blob/文件夹中，名字用计算获得的
sha1(filename,content)命名。**

#### Fields

1. `File sourcefile` - 根据源文件生成blob
2. `String filename` - 文件的名字，这里直接用源文件的名字
3.  `byte[] content` -文件的内容
4. `String id` - 用sha1(filename,content)生成，只有文件名和内容都一样才能生成同样的sha1值，这个值用来命名Blob文件
5. `File file` - 生成的blob文件，存储在Blob文件夹中，文件名字采用id值生成。


## Algorithms
### gitlet init
**初始化整个gitlet仓库**  
1. 创建.gitlet文件夹和里面的子文件夹
2. 创建初始提交，并将初始提交保存到Commit文件夹中
3. 将master指针和HEAD指针指向这个提交
4. 改变git log

## Persistence
### 文件保存架构
```
.gitlet
       StagingArea
       Commit
       Blob
       ref/heads/[master] 
       [HEAD]       
```
* `StagingArea` 文件夹，存储在暂存区中的blob文件
*  `Commit` 文件夹，存储Commit文件
*  `Blob` 文件夹，存储已经提交的blob文件
*  `ref/heads/[master]` 存储master指针
*  `[HEAD]` 存储HEAD指针

