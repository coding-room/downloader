package cr.downloader.http;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author Beldon
 */
@Service
public class DefaultDownloadFileFetcher implements DownloadFileFetcher {

    private static final String CONTENT_LENGTH_KEY = "content-length";
    private static final String FILE_NAME_KEY = "content-disposition";
    private static final String ACCEPT_RANGES_KEY = "accept-ranges";
    private static final String CONTENT_TYPE_KEY = "content-type";

    @Override
    public DownloadFile fetch(String url) throws IOException {
        URL realUrl = new URL(url);

        URLConnection connection = realUrl.openConnection();
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        connection.setConnectTimeout(10000);
        connection.connect();

        Map<String, List<String>> headerFields = connection.getHeaderFields();
        DownloadFile downloadFile = new DownloadFile();
        headerFields.forEach((k, v) -> {
            if (StringUtils.isEmpty(k) || CollectionUtils.isEmpty(v)) {
                return;
            }
            String key = k.toLowerCase();
            switch (key) {
                case CONTENT_LENGTH_KEY:
                    downloadFile.setSize(Long.valueOf(v.get(0)));
                    break;
                case FILE_NAME_KEY:
                    String name = v.get(0);
                    name = name.substring(name.indexOf("=") + 1);
                    downloadFile.setFileName(name);
                    break;
                case CONTENT_TYPE_KEY:
                    downloadFile.setContentType(v.get(0));
                    break;
                case ACCEPT_RANGES_KEY:
                    downloadFile.setCanRanges(true);
            }
        });

        if (StringUtils.isEmpty(downloadFile.getFileName())) {
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            int i = fileName.lastIndexOf("?");
            if (i > 0) {
                fileName = fileName.substring(0, i);
            }
            downloadFile.setFileName(fileName);

        }
        return downloadFile;
    }
}
