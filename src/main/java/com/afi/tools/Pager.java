package com.afi.tools;

import java.util.List;

public class Pager {

	private int buttonsToShow = 5;

	private int startPage;

	private int endPage;
	
	private int currentPage;
	
	private int totalPages;
	
	private int number;
	
	private List<Object> list;

	public Pager(List<Object> list, int totalPages, int currentPage, int buttonsToShow) {
		this.list = list;
		setButtonsToShow(buttonsToShow);

		int halfPagesToShow = getButtonsToShow() / 2;

		if (totalPages <= getButtonsToShow()) {
			setStartPage(1);
			setEndPage(totalPages);

		} else if (currentPage - halfPagesToShow <= 0) {
			setStartPage(1);
			setEndPage(getButtonsToShow());

		} else if (currentPage + halfPagesToShow == totalPages) {
			setStartPage(currentPage - halfPagesToShow);
			setEndPage(totalPages);

		} else if (currentPage + halfPagesToShow > totalPages) {
			setStartPage(totalPages - getButtonsToShow() + 1);
			setEndPage(totalPages);

		} else {
			setStartPage(currentPage - halfPagesToShow);
			setEndPage(currentPage + halfPagesToShow);
		}

	}

	public int getButtonsToShow() {
		return buttonsToShow;
	}

	public void setButtonsToShow(int buttonsToShow) {
		if (buttonsToShow % 2 != 0) {
			this.buttonsToShow = buttonsToShow;
		}
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "Pager [startPage=" + startPage + ", endPage=" + endPage + "]";
	}

}