package com.pocketcocktails.pocketbar.data

import com.pocketcocktails.pocketbar.data.network.ApiService
import com.pocketcocktails.pocketbar.presentation.model.CocktailInfoModel
import com.pocketcocktails.pocketbar.presentation.model.CocktailListItemModel
import com.pocketcocktails.pocketbar.presentation.model.IngredientModel
import com.pocketcocktails.pocketbar.domain.CocktailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class CocktailsRepositoryImpl @Inject constructor(
    private val api: ApiService,
) : CocktailsRepository {

    @Throws(IOException::class)
    override suspend fun getDrinkByName(searchText: String): List<CocktailListItemModel> {
        val cocktailsList = api.getDrinkByName(searchText)
        return withContext(Dispatchers.Default) {
            cocktailsList.cocktailsList.map { cocktail ->
                CocktailListItemModel(
                    cocktail.idDrink,
                    cocktail.strDrink,
                    cocktail.strDrinkThumb,
                    false
                )
            }.toList()
        }
    }

    @Throws(IOException::class)
    override suspend fun getFavoriteById(id: String): CocktailListItemModel {
        val favoriteItemList = api.getDrinkById(id).cocktailsList.first()
        return withContext(Dispatchers.Default) {
            CocktailListItemModel(
                favoriteItemList.idDrink,
                favoriteItemList.strDrink,
                favoriteItemList.strDrinkThumb,
                true
            )
        }
    }

    override suspend fun getDrinkById(id: String): CocktailInfoModel {
        val drink = api.getDrinkById(id).cocktailsList.first()

        val ingredients = arrayListOf<IngredientModel>()
        var ingrid = IngredientModel()

        if (!drink.strIngredient1.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient1)
            if (!drink.strMeasure1.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure1)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient2.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient2)
            if (!drink.strMeasure2.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure2)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient3.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient3)
            if (!drink.strMeasure3.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure3)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient4.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient4)
            if (!drink.strMeasure4.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure4)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient5.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient5)
            if (!drink.strMeasure5.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure5)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient6.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient6)
            if (!drink.strMeasure6.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure6)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient7.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient7)
            if (!drink.strMeasure7.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure7)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient8.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient8)
            if (!drink.strMeasure8.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure8)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient9.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient9)
            if (!drink.strMeasure9.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure9)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient10.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient10)
            if (!drink.strMeasure10.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure10)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient11.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient11)
            if (!drink.strMeasure11.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure11)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient12.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient12)
            if (!drink.strMeasure12.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure12)
                ingredients.add(ingrid)
            }
        }

        if (!drink.strIngredient13.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient13)
            if (!drink.strMeasure13.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure13)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient14.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient14)
            if (!drink.strMeasure14.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure14)
            }
            ingredients.add(ingrid)
        }

        if (!drink.strIngredient15.isNullOrEmpty()) {
            ingrid = ingrid.copy(name = drink.strIngredient15)
            if (!drink.strMeasure15.isNullOrEmpty()) {
                ingrid = ingrid.copy(measure = drink.strMeasure15)
            }
            ingredients.add(ingrid)
        }

        val instructions = arrayListOf<String>()
        instructions.add(drink.strInstructions)
        instructions.add(drink.strInstructionsDE)
        instructions.add(drink.strInstructionsES)
        instructions.add(drink.strInstructionsFR)
        instructions.add(drink.strInstructionsIT)

        val locale = arrayListOf<String>()
        locale.add("EN")
        locale.add("DE")
        locale.add("ES")
        locale.add("FR")
        locale.add("IT")

        val guide: Map<String, String> = locale.zip(instructions).toMap()

        return withContext(Dispatchers.Default) {
            CocktailInfoModel(
                idDrink = drink.idDrink,
                name = drink.strDrink,
                drinkThumb = drink.strDrinkThumb,
                drinkAlternate = drink.strDrinkAlternate,
                tags = drink.strTags,
                video = drink.strVideo,
                category = drink.strCategory,
                IBA = drink.strIBA,
                alcoholic = drink.strAlcoholic,
                glass = drink.strGlass,
                guide = guide,
                instructionsZHHANS = drink.strInstructionsZHHANS,
                instructionsZHHANT = drink.strInstructionsZHHANT,
                ingredient = ingredients,
                imageSource = drink.strImageSource,
                imageAttribution = drink.strImageAttribution,
                creativeCommonsConfirmed = drink.strCreativeCommonsConfirmed,
                dateModified = drink.dateModified,
            )
        }
    }

    override suspend fun getDrinkByBase(baseName: String): List<CocktailListItemModel> {
        val cocktailsList = api.getDrinkByIngredient(baseName)
        return withContext(Dispatchers.Default) {
            cocktailsList.cocktailsList.map { cocktail ->
                CocktailListItemModel(
                    cocktail.idDrink,
                    cocktail.strDrink,
                    cocktail.strDrinkThumb,
                    false
                )
            }.toList()
        }
    }
}