package cdu.zch.rpc.compress;

import cdu.zch.rpc.extension.SPI;

/**
 * @author Zch
 * @date 2023/7/21
 **/
@SPI
public interface Compress {

    byte[] compress(byte[] bytes);


    byte[] decompress(byte[] bytes);

}
