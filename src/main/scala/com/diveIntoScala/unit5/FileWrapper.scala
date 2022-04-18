package com.diveIntoScala.unit5

import java.io.{File => JFile}

/**
 * 用于可以在当前文件下生成新文件的方法
 *
 * @param file
 * @author chenwu on 2022.4.15
 */
class FileWrapper(val file: JFile) {

  def \(newPath: String) = {
    val newFile = new JFile(file, newPath)
    if(!newFile.exists()){
        newFile.createNewFile()
    }
    new FileWrapper(newFile)
  }

  override def toString: String = file.getCanonicalPath
}

object FileWrapper {

  def apply(file: JFile) = {
    new FileWrapper(file)
  }
}
