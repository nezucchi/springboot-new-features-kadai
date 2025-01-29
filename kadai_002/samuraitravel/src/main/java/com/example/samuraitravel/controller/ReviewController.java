package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("/houses/{houseId}/reviews")
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final ReviewService reviewService;

	public ReviewController(ReviewRepository reviewRepository, HouseRepository houseRepository,
			ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
		this.reviewService = reviewService;
	}

	@GetMapping
	public String index(@PathVariable(name = "houseId") Integer houseId,
			@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {
                      // Houseクラスのhouseインスタンスを作成、houseRepository.getReferenceByIdメソッド(引数はhouseId)をセット
                      // Page<Review>型のreviewPageインスタンスを作成、reviewRepository.findByHouseOrderByCreatedAtDescメソッド(引数はhouse, pageable)をセット
		House house = houseRepository.getReferenceById(houseId);
		Page<Review> reviewPage = reviewRepository.findByHouseOrderByCreatedAtDesc(house, pageable);

                      // modelのaddAttributeを使用して引数に"house", houseをセット。
                      // modelのaddAttributeを使用して引数に"reviewPage", reviewPageをセット。
		model.addAttribute("house", house);
		model.addAttribute("reviewPage",reviewPage);

                      // "reviews/index"を返却
		return "reviews/index";
	}

	@GetMapping("/register")
	public String register(@PathVariable(name = "houseId") Integer houseId, Model model) {
                      // Houseクラスのhouseインスタンスを作成、houseRepository.getReferenceByIdメソッド(引数はhouseId)をセット
		House house = houseRepository.getReferenceById(houseId);

                      // modelのaddAttributeを使用して引数に"house", houseをセット。
		model.addAttribute("house", house);
                      // modelのaddAttributeを使用して引数に"reviewRegisterForm", new ReviewRegisterForm()をセット。
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());

                      // "reviews/register"を返却
		return "reviews/register";
	}

	@PostMapping("/create")
	public String create(@PathVariable(name = "houseId") Integer houseId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
                      // Houseクラスのhouseインスタンスを作成、houseRepository.getReferenceByIdメソッド(引数はhouseId)をセット
                      // Userクラスのuserインスタンスを作成、userDetailsImpl.getUser()をセット
		House house = houseRepository.getReferenceById(houseId);
		User user = userDetailsImpl.getUser();

                      // 分岐文でbindingResultの判定を作成
                      // エラーチェックに引っかかった場合
                      // modelのaddAttributeを使用して引数に"house", houseをセット。
                      // "reviews/register"を返却
		if(bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			return "reviws/register";
			
		}

                      // favoriteServiceのcreateメソッドを使用(引数はhouse, user, reviewRegisterForm)
                      // redirectAttributesのredirectAttributesメソッドを使用(引数は"successMessage", "レビューを投稿しました。")
		reviewService.create(house, user, reviewRegisterForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");

                      // "redirect:/houses/{houseId}"を返却
		return "redirect:/houses/{houseId}";
	}

	@GetMapping("/{reviewId}/edit")
	public String edit(@PathVariable(name = "houseId") Integer houseId,
			@PathVariable(name = "reviewId") Integer reviewId, Model model) {
                      // Houseクラスのhouseインスタンスを作成、houseRepository.getReferenceByIdメソッド(引数はhouseId)をセット
                      // Reviewクラスのreviewインスタンスを作成、reviewRepository.getReferenceByIdメソッド(引数はreviewId)をセット
		House house = houseRepository.getReferenceById(houseId);
		Review review = reviewRepository.getReferenceById(reviewId);

                      // ReviewEditFormクラスのreviewEditFormインスタンスを作成、コンストラクタはreview.getId(), review.getScore(), review.getContent()をセット
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getScore(), review.getContent());

                      // modelのaddAttributeを使用して引数に"house", houseをセット。
                      // modelのaddAttributeを使用して引数に"review", reviewをセット。
                      // modelのaddAttributeを使用して引数に"reviewEditForm", reviewEditFormをセット。
		model.addAttribute("house", house);
		model.addAttribute("review", review);
		model.addAttribute("reviewEditForm", reviewEditForm);
		

                      // "reviews/edit"を返却
		return "reviews/edit";
	}

	@PostMapping("/{reviewId}/update")
	public String update(@PathVariable(name = "houseId") Integer houseId,
			@PathVariable(name = "reviewId") Integer reviewId,
			@ModelAttribute @Validated ReviewEditForm reviewEditForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
                      // Houseクラスのhouseインスタンスを作成、houseRepository.getReferenceByIdメソッド(引数はhouseId)をセット
                      // Reviewクラスのreviewインスタンスを作成、reviewRepository.getReferenceByIdメソッド(引数はreviewId)をセット
		House house = houseRepository.getReferenceById(houseId);
		Review review = reviewRepository.getReferenceById(reviewId);

                      // 分岐文でbindingResultの判定を作成
                      // エラーチェックに引っかかった場合
                      // modelのaddAttributeを使用して引数に"house", houseをセット。
                      // modelのaddAttributeを使用して引数に"review", reviewをセット。
                      // "reviews/edit"を返却
		if(bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			model.addAttribute("review", review);
			return "reviews/edit";
		}

                      // reviewServiceのupdateメソッドを使用(引数はreviewEditForm)
                      // redirectAttributesのredirectAttributesメソッドを使用(引数は"successMessage", "レビューを編集しました。")
		reviewService.update(reviewEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。" );

                      // "redirect:/houses/{houseId}"を返却
		return "redirect:/houses/{houseId}";
	}

	@PostMapping("/{reviewId}/delete")
	public String delete(@PathVariable(name = "reviewId") Integer reviewId, RedirectAttributes redirectAttributes) {
                      // reviewRepositoryのdeleteByIdメソッドを使用(引数はreviewId)
		reviewRepository.deleteById(reviewId);

                      // redirectAttributesのredirectAttributesメソッドを使用(引数は"successMessage", "レビューを削除しました。")
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました");

                      // "redirect:/houses/{houseId}"を返却
		return "redirect:/houses/{houseId}";
	}
}
