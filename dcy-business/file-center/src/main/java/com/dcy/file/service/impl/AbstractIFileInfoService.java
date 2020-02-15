package com.dcy.file.service.impl;

import com.dcy.file.mapper.FileInfoMapper;
import com.dcy.file.model.FileInfo;
import com.dcy.file.service.IFileInfoService;
import com.dcy.file.utils.FileUtil;
import com.dcy.web.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dcy
 * @since 2019-09-18
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public abstract class AbstractIFileInfoService extends BaseServiceImpl<FileInfoMapper, FileInfo> implements IFileInfoService {

    private static final String FILE_SPLIT = ".";

    @Autowired
    private FileInfoMapper fileInfoMapper;


    @Override
    public FileInfo upload(MultipartFile file) throws Exception {
        FileInfo fileInfo = FileUtil.getFileInfo(file);
        FileInfo oldFileInfo = fileInfoMapper.selectById(fileInfo.getId());
        if (oldFileInfo != null) {
            return oldFileInfo;
        }
        if (!fileInfo.getName().contains(FILE_SPLIT)) {
            throw new IllegalArgumentException("缺少后缀名");
        }
        uploadFile(file, fileInfo);
        // 设置文件来源
        fileInfo.setSource(fileType());
        // 将文件信息保存到数据库
        fileInfoMapper.insert(fileInfo);
        return fileInfo;
    }

    @Override
    public void deleteFile(String id) {
        FileInfo fileInfo = fileInfoMapper.selectById(id);
        if (fileInfo != null) {
            fileInfoMapper.deleteById(fileInfo.getId());
            this.deleteFile(fileInfo);
        }
    }


    /**
     * 文件来源
     *
     * @return
     */
    protected abstract String fileType();

    /**
     * 上传文件
     *
     * @param file
     * @param fileInfo
     */
    protected abstract void uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception;


    /**
     * 删除文件资源
     *
     * @param fileInfo
     * @return
     */
    protected abstract boolean deleteFile(FileInfo fileInfo);

}
