package com.example.samuraitravel.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("/houses")
public class HouseController {
	private final HouseRepository houseRepository;
	private final ReviewRepository reviewRepository;
	private final ReviewService reviewService;
	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;

	public HouseController(HouseRepository houseRepository, ReviewRepository reviewRepository,
			ReviewService reviewService, FavoriteRepository favoriteRepository, FavoriteService favoriteService) {
		this.houseRepository = houseRepository;
		this.reviewRepository = reviewRepository;
		this.reviewService = reviewService;
		this.favoriteRepository = favoriteRepository;
		this.favoriteService = favoriteService;
	}

	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "area", required = false) String area,
			@RequestParam(name = "price", required = false) Integer price,
			@RequestParam(name = "order", required = false) String order,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		Page<House> housePage;

		if (keyword != null && !keyword.isEmpty()) {
			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%",
						"%" + keyword + "%", pageable);
			} else {
				housePage = houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%",
						"%" + keyword + "%", pageable);
			}
		} else if (area != null && !area.isEmpty()) {
			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
			} else {
				housePage = houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
			}
		} else if (price != null) {
			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
			} else {
				housePage = houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
			}
		} else {
			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findAllByOrderByPriceAsc(pageable);
			} else {
				housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);
			}
		}

		model.addAttribute("housePage", housePage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("area", area);
		model.addAttribute("price", price);
		model.addAttribute("order", order);

		return "houses/index";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
                      
		House house = houseRepository.getReferenceById(id);
                      
                      // boolean型のレビュー済みフラグ(reviewFlag)を作成、falseをセット
                      // boolean型のお気に入り追加フラグ(favoriteFlag)を作成、falseをセット
                      // Favoriteクラスのfavoriteインスタンスを作成、nullをセット
		boolean reviewFlag = false;

                      // 分岐文を作成、userDetailsImplがnull以外(ログイン済み)の場合
                      // Userクラスのuserインスタンスを作成、userDetailsImpl.getUser()をセット
                      // reviewFlagへreviewServiceのhasUserAlreadyReviewedメソッド(引数house, user)をセット
                      // favoriteFlagへfavoriteServiceのisFavoriteメソッド(引数house, user)をセット
                      // 分岐文を作成、favoriteFlagがtrue(家に対してのお気に入りが存在している)の場合
                      // favoriteへfavoriteRepositoryのfindByHouseAndUserメソッド(引数house, user)をセット
		if(userDetailsImpl != null) {
			User user = userDetailsImpl.getUser();
			reviewFlag = reviewService.hasUserAlreadyReviewed(house, user);
			
		}

                      // modelのaddAttributeを使用して引数に"reviewFlag", reviewFlagをセット。
                      // modelのaddAttributeを使用して引数に"favoriteFlag", favoriteFlagをセット。
                      // modelのaddAttributeを使用して引数に"favorite", favoriteをセット。
		model.addAttribute("reviwFlag", reviewFlag);

                      // List<Review>型のreviewListインスタンスを作成、reviewRepository.findTop6ByHouseOrderByCreatedAtDescメソッド(引数はhouse)をセット
                      // modelのaddAttributeを使用して引数に"reviewList", reviewListをセット。
                      // int型のtotalCountを作成、reviewRepository.countByHouse(引数はhouse)をセット
                      // modelのaddAttributeを使用して引数に"totalCount", totalCountをセット。
		List<Review> reviewLisst = reviewRepository.findTop6ByHouseOrderByCreatedAtDesc(house);
		model.addAttribute("reviewList", reviewLisst);
		long totalCount = reviewRepository.countByHouse(house);
		model.addAttribute("totalCount", totalCount);
                      
		model.addAttribute("house", house);
		model.addAttribute("reservationInputForm", new ReservationInputForm());
                      
		return "houses/show";
	}
}
