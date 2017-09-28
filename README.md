# HttpClientUtil
这个例子是使用java语言编写的

导入了HttpClient4.5版本的jar包

目前提供方法

1、GET方法

String doGetMethod(String url, Map<String, String> headerMap) throws IOException

2、POST方法

String doPostMethod(String url, String data, Map<String, String> headerMap) throws IOException

String doPostMethod(String url, Map<String, Object> data, Map<String, String> headerMap) throws IOException

3、文件下载方法

String getFileByUrl(String url, String savePath) throws Exception

