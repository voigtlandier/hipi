package hipi.opencv;

import org.apache.hadoop.io.Writable;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class OpenCVMatWritable implements Writable {

  private Mat mat = null;
  
  public class OpenCVMatWritable() {
    // TODO: verify that this creates a non-null 2D array
    mat = new Mat();
    assert mat != null;
    int dims = mat.dims();
    assert (dims == 1 || dims == 2); // handle only 1- or 2-D arrays
  }

  public class OpenCVMatWritable(Mat mat) {
    setMat(mat);
  }

  public void setMat(Mat mat) throws IllegalArgumentException {
    if (mat == null) {
      throw new IllegalArgumentException("Must provide valid non-null Mat object.");
    }
    int dims = mat.dims();
    if (!(dims == 1 || dims == 2) ) {
      throw new IllegalArguemntException("Currently supports only 1D or 2D arrays.");
    }
    this.mat = mat;
  }

  public Mat getMat() {
    return mat;
  }

  public void write(DataOutput out) throws IOException {

    assert mat != null;
    int dims = mat.dims();
    assert (dims == 1 || dims == 2); // handle only 1- or 2-D arrays

    int type = mat.type();
    out.writeInt(type);
    out.writeInt(mat.rows());
    out.writeInt(mat.cols());
    
    int elms = mat.rows()*mat.cols();
    if (elms > 0) {
      int depth = CvType.depth(t);
      switch (depth) {
      case CvType.CV_8U:
      case CVType.CV_8S:
	byte[] data = new byte[elms];
	mat.get(0, 0, data);
	output.write(data);
	break;
      case CvType.CV_16U:
      case CvType.CV_16S:
	short[] data = new short[elms]; // 2 bytes per short
	mat.get(0, 0, data);
	data[] dataAsBytes = ByteUtils.shortArrayToByteArray(data);
	output.write(dataAsBytes);
	break;
      case CvType.CV_32S:
	// TODO: int[] <=> byte[]
	assert false;
	break;
      case CvType.CV_32F:
	// TODO: float[] <=> byte[]
	assert false;
	break;
      case CvType.CV_64F:
	// TOOD: double[] <=> byte[]
	assert false;
	break;
    default:
      throw new IOException("Unsupported matrix type [" + type + "].");
      }
    }

  }
       
  public void readFields(DataInput in) throws IOException {
    // TODO
    assert false;
  }  

}

