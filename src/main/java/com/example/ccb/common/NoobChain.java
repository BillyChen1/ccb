package com.example.ccb.common;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class NoobChain {

    private List<Block> blockchain = new ArrayList<Block>();
    private int difficulty = 5;

    public static void main(String[] args) {
        //add our blocks to the blockchain ArrayList:
        NoobChain chain = new NoobChain();
        List<Block> blockchain = chain.blockchain;
        int difficulty = chain.difficulty;

        chain.add("Hi im the first block", "a");
        System.out.println("Trying to Mine block 1... ");
        blockchain.get(0).mineBlock(difficulty);

        chain.add("Yo im the second block", "b");
        System.out.println("Trying to Mine block 2... ");
        blockchain.get(1).mineBlock(difficulty);

        chain.add("Hey im the third block", "c");
        System.out.println("Trying to Mine block 3... ");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("\nBlockchain is Valid: " + chain.isChainValid());


        String blockchainJson = JSON.toJSONString(blockchain);

//        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);
    }

    /**
     * 将一个新数据加入区块链
     * @param data 数据字符串
     */
    public void add(String data, String certificateNum) {
        Block block = new Block(data, blockchain.isEmpty() ? "0" : blockchain.get(blockchain.size() - 1).hash, certificateNum);
        blockchain.add(block);
    }


    public Block findBlockByCertificateNum(String certificateNum) {
        for (Block block : blockchain) {
            if (block.getCertificateNum().equals(certificateNum)) {
                return block;
            }
        }
        return null;
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
//            //check if hash is solved
//            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
//                System.out.println("This block hasn't been mined");
//                return false;
//            }
        }
        return true;
    }
}
