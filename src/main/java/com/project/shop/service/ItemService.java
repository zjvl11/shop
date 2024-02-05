package com.project.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import com.project.shop.dto.ItemSearchDto;
import com.project.shop.dto.MainItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.shop.dto.ItemFormDto;
import com.project.shop.dto.ItemImgDto;
//import com.project.shop.dto.ItemSearchDTO;
//import com.project.shop.dto.MainItemDTO;
import com.project.shop.entity.Item;
import com.project.shop.entity.ItemImg;
import com.project.shop.repository.ItemImgRepository;
import com.project.shop.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;


    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품등록
        Item item = itemFormDto.createItem(); // 싱품 등록 폼으로부터 입력 받은 데이터를 이용해 item 객체를 생성
        itemRepository.save(item); //상품 데이터 저장

        //이미지 등록
        for(int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0) { //첫 반째 이미지일 경우 대표 상품 이미지 여부 값을 Y로 세팅. 나머지 상품 이미지는 N으로 설정
                itemImg.setRepimgYn("Y");
            } else {
                itemImg.setRepimgYn("N");

            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 상품의 이미지 정보를 저장
        }
        return item.getId();
    }

    //등록된 상품 불러오기
    @Transactional(readOnly = true) //상품 데이터를 읽어오는 트랜잭션-읽기 전용, JPA가 변경감지를 수행하지 않아 성능 향상 시킬 수 있다
    public ItemFormDto getItemDetail(Long itemId) {

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 해당 상품의 이미지 조회, 등록순으로 가지고 오기 위해 상품 이미지 아이디 오름차순으로 가져옴
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for(ItemImg itemImg : itemImgList) { //조회한 ItemImg 엔티티를 ItemImgDTO 객체로 만들어서 리스트에 추가
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new); // 상품의 아이디를 통해 상품 엔티티를 조회, 존재하지 않을 때는 EntityNotFoundException 발생

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }


    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new); //상품 등록 화면으로부터 전달 받은 상품 아이디를 이용해 상품 엔티티 조회
        item.updateItem(itemFormDto); //상품 등록 화면으로부터 전달 받은 itemFormDto를 통해 상품 엔티티를 업데이트

        List<Long> itemImgIds = itemFormDto.getItemImgIds(); //상품 이미지 아이디 리스트를 조회

        //이미지 등록
        for(int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
            // 상품 이미지를 업데이트 하기 위해 updateItemImg() 메소드에 상품 이미지 아이디와 상품 이미지 파일 정보를 파라미터로 전달한다.
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }


    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
