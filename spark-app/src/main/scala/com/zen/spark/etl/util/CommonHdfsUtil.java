package com.zen.spark.etl.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * hdfs连接池管理
 *
 * @author moshengsong
 * @date 2017-05-13
 */
public class CommonHdfsUtil {
	private final Logger logger = Logger.getLogger(CommonHdfsUtil.class);
	private Configuration conf;
	private FileSystem fs;
	private static volatile CommonHdfsUtil instance = null;

	public static CommonHdfsUtil getInstance() {
		if (instance == null) {
			// 同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
			synchronized (CommonHdfsUtil.class) {
				// 未初始化，则初始instance变量
				if (instance == null) {
					instance = new CommonHdfsUtil();
				}
			}
		}
		return instance;
	}

	/**
	 * @description 初始化hdfs配置
	 * @param
	 * @return
	 * @exception @author zhengxinbo
	 * @date 2017-05-15
	 */
	private CommonHdfsUtil() {

		try {
			conf = new Configuration();
			// 这句话很关键，这些信息就是hadoop配置文件中的信息
			// conf.set("mapred.job.tracker", mapred_job_tracker);
			//conf.set("fs.default.name", fs_default_name);
			fs = FileSystem.get(conf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

	public FileSystem getFs() {
		return fs;
	}

	/**
	 * 关闭hdfs文件系统
	 * 
	 * @author Administrator void
	 */
	public void colse() {
		try {
			if (fs != null){
				fs.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 判断hadoop 文件是否存在
	 * @param dir
	 * @return
	 */
	public Boolean isExists(String dir) {
		boolean b = false;
		Path p = new Path(dir);
		try {
			b = fs.exists(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	/**
	 * 判断是否是目录
	 * 
	 * @param dir
	 * @return
	 */
	public Boolean isDirectory(String dir) {
		boolean b = false;
		Path p = new Path(dir);
		try {
			b = fs.isDirectory(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 判断是否是文件
	 * 
	 * @param fileName
	 * @return
	 */
	public Boolean isFile(String fileName) {
		boolean b = false;
		Path p = new Path(fileName);
		try {
			b = fs.isFile(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * hdfs创建新的文件,并往写数据
	 * 
	 * @author Administrator
	 * @param dst
	 *            void
	 */
	public void createNewHDFSFile(String dst, String content) {
		Path dstPath = new Path(dst); // 目标路径
		FSDataOutputStream os = null;
		try {
			// fs.create(dstPath,new
			// FsPermission((short)00777),false,1204,(short)1, 1048576,null);
			if (fs.exists(dstPath)) {
				fs.createNewFile(dstPath);
				os = fs.create(dstPath);
				os.write(content.getBytes(StandardCharsets.UTF_8));
				System.out.println("文件创建成功！");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 删除hdfs文件
	 * 
	 * @author Administrator
	 * @param dst
	 * @return boolean
	 */
	public boolean deleteHDFSFile(String dst) {
		Path path = new Path(dst);
		boolean isDeleted = false;
		try {
			isDeleted = fs.delete(path,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return isDeleted;
	}

	/**
	 * 读取hdfs文件流
	 * 
	 * @description
	 * @param
	 * @return InputStream
	 * @exception @author
	 *                zhengxinbo
	 * @date 2017年7月12日
	 */
	public InputStream readHDFSFileStream(String dst) {
		InputStream in = null;
		try {
			in = fs.open(new Path(dst));
			IOUtils.copyBytes(in, System.out, 4096, false);
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			 IOUtils.closeStream(in);
		}
		return in;
	}

	/**
	 * 读取hdfs文件数据
	 * 
	 * @author Administrator
	 * @param dst
	 * @return byte[]
	 */
	public byte[] readHDFSFile(String dst) {
		// check if the file exists
		Path path = new Path(dst);
		FSDataInputStream is = null;
		try {
			if (fs.exists(path)) {
				is = fs.open(path);
				// get the file info to create the buffer
				FileStatus stat = fs.getFileStatus(path);
				// create the buffer
				byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
				is.readFully(0, buffer);
				is.close();
				return buffer;
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				if (is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 创建hdfs目录
	 * 
	 * @author Administrator
	 * @param path
	 * @return boolean
	 */
	public boolean mkdir(String path) {
		Path srcPath = new Path(path);
		boolean isok = false;
		try {
			if (!fs.exists(srcPath)) {
				isok = fs.mkdirs(srcPath);
				System.out.println("create dir ok!");
			} else {
				System.out.println(path + "已存在！！！");
			}

		} catch (IOException e) {
			System.out.println("create dir failure");
			e.printStackTrace();
		}
		return isok;
	}

	/**
	 * 上传本地文件到hdfs
	 * 
	 * @author Administrator
	 * @param source
	 * @param target
	 * @param delLocalFile
	 *            是否删除本地文件
	 * @param overwrite
	 *            void hdfs文件存在，是否覆盖
	 */
	public boolean uploadLocalFile2HDFS(String source, String target, boolean delLocalFile, boolean overwrite) {
		boolean bool = false;
		try {
			Path srcPath = new Path(source); // 原路径
			Path dstPath = new Path(target); // 目标路径
			// 调用文件系统的文件复制函数,前面参数是指是否删除原文件，true为删除，默认为false
			fs.copyFromLocalFile(delLocalFile, overwrite, srcPath, dstPath);
			bool = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bool = false;
		} finally {
			 colse();
		}
		return bool;
	}

	/**
	 * 下载hdfs文件到本地
	 * 
	 * @author Administrator
	 * @param hdfsFileName
	 * @param localFileName
	 * @param deldsc
	 *            是否删除hdfs文件
	 */
	public boolean downloadLocalFile(String hdfsFileName, String localFileName, boolean deldsc) {
		boolean bool = false;
		try {
			fs.copyToLocalFile(deldsc, new Path(hdfsFileName), new Path(localFileName));
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
			bool = false;
		}
		return bool;
	}

	/**
	 * hdfs内部文件移动
	 * 
	 * @author Administrator
	 * @param src
	 * @param target
	 *            void
	 */
	public void remane(String src, String target) {
		Path srcPath = new Path(src); // 原路径
		Path dstPath = new Path(target); // 目标路径
		try {
			if (!fs.exists(dstPath.getParent())){
				fs.mkdirs(dstPath.getParent());
			}
			else if (fs.exists(dstPath)){
				fs.delete(dstPath);
			}
			fs.rename(srcPath, dstPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * hdfs内部文件追加
	 * 
	 * @author Administrator
	 * @param src
	 * @param target
	 *            void
	 */
	public void appendFile(String src, String target) {
		FSDataInputStream in = null;
		FSDataOutputStream out = null;
		try {
			in = fs.open(new Path(src));
			if (fs.exists(new Path(target))) {
				out = fs.append(new Path(target));
			} else {
				out = fs.create(new Path(target));
			}
			out.write("\r\n".getBytes(StandardCharsets.UTF_8));
			IOUtils.copyBytes(in, out, 4096, true);
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				IOUtils.closeStream(out);
			}
			if (in != null) {
				IOUtils.closeStream(in);
			}
		}
	}

	public void appendFile(String src, String target, String line) {
		FSDataInputStream in = null;
		FSDataOutputStream out = null;
		try {
			in = fs.open(new Path(src));
			if (fs.exists(new Path(target))) {
				out = fs.append(new Path(target));
			} else {
				out = fs.create(new Path(target));
			}
			out.write((line + "\r\n").getBytes(StandardCharsets.UTF_8));
			IOUtils.copyBytes(in, out, 4096, true);
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (out != null) {
				IOUtils.closeStream(out);
			}
			if (in != null) {
				IOUtils.closeStream(in);
			}
		}
	}

	/**
	 * 获取一个目录的所有下级文件
	 * 
	 * @author Administrator
	 * @param dir
	 * @return Path[]
	 */
	public Path[] listAll(String dir) {
		try {
			FileStatus[] stats = fs.listStatus(new Path(dir));
			Path[] listPath = FileUtil.stat2Paths(stats);
			for (Path p : listPath) {
				System.out.println(p);
			}
			return listPath;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取目录下所有的文件
	 * 
	 * @author Administrator
	 * @param dir
	 * @return List<String>
	 */
	public List<String> listAllFiles(String dir) {
		List<String> files = new ArrayList<String>();
		try {
			FileStatus[] stats = fs.listStatus(new Path(dir));
			for (FileStatus stat : stats) {
				if (stat.isDirectory()) {

					listAllFiles(stat.getPath().toString());
				} else {
					files.add(stat.getPath().toString());
				}

			}
			return files;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		CommonHdfsUtil hdfsUtil = getInstance();
		hdfsUtil.listAll("/test/gather");
	}

}
