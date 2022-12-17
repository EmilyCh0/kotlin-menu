package menu

import camp.nextstep.edu.missionutils.Randoms

class LunchMenuRecommender(
    private val dislikeMenus: Map<String, List<String>>,
    private val totalMenus: List<List<String>>,
    private val coaches: List<String>
) {
    private val recommendedMenu = mutableMapOf<String, MutableList<String>>()
    private val categoryChosen = mutableListOf<Int>()

    init {
        coaches.forEach {
            recommendedMenu[it] = mutableListOf()
        }
    }

    fun chooseOneWeekMenu() {
        chooseOneDayMenu()
    }

    private fun chooseOneDayMenu() {
        val categoryIndex = chooseCategory()
        chooseMenuForEachCoach(totalMenus[categoryIndex])
    }

    private fun chooseCategory(): Int {
        val randomNumber = Randoms.pickNumberInRange(1, 5)
        if (categoryChosen.count { it == randomNumber } == 2) {
            chooseCategory()
        }
        categoryChosen.add(randomNumber)
        return randomNumber
    }

    private fun chooseMenuForEachCoach(categoryMenus: List<String>) {
        coaches.forEach {
            while (true) {
                val randomMenu = chooseMenu(categoryMenus)
                val dislike = dislikeMenus[it]?.let { dislikeMenuOfThisCoach ->
                    isDislikeMenu(
                        dislikeMenuOfThisCoach,
                        randomMenu
                    )
                } ?: false
                val duplicateMenu = recommendedMenu[it]?.let { recommendedMenuOfThisCoach ->
                    isAlreadyRecommended(
                        recommendedMenuOfThisCoach,
                        randomMenu
                    )
                } ?: false
                if (!dislike && !duplicateMenu) {
                    recommendedMenu[it]?.add(randomMenu)
                    break
                }
            }
        }
    }

    private fun isDislikeMenu(dislikeMenus: List<String>, menuChosen: String): Boolean {
        return dislikeMenus.contains(menuChosen)
    }

    private fun isAlreadyRecommended(recommendedMenus: List<String>, menuChosen: String): Boolean {
        return recommendedMenus.contains(menuChosen)
    }

    private fun chooseMenu(categoryMenus: List<String>): String {
        return Randoms.shuffle(categoryMenus)[0]
    }
}