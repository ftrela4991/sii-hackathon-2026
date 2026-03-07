# PrestaShop Product API Request Structure

## Overview
This is the form structure used by PrestaShop's admin panel when creating/updating products. The request is a **nested array-based form submission** (not REST JSON).

## Request Details
- **Method**: POST
- **Endpoint**: `/admin_hackathon/index.php/sell/catalog/products-v2/{id}/edit`
- **Content-Type**: `application/x-www-form-urlencoded`
- **Authentication**: Session cookies (PHPSESSID)

## Complete Product Structure

```
product[header][cover_thumbnail]     = {image_url}
product[header][name][{lang_id}]     = {product_name}
product[header][type]                = standard|virtual|pack
product[header][active]              = 0|1
product[header][initial_type]        = standard

product[description][description_short][{lang_id}] = {short_description_html}
product[description][description][{lang_id}]       = {long_description_html}
product[description][categories][product_categories][{idx}][id]           = {category_id}
product[description][categories][product_categories][{idx}][name]         = {category_name}
product[description][categories][product_categories][{idx}][display_name] = {display_name}
product[description][categories][default_category_id]                     = {category_id}
product[description][manufacturer]   = {manufacturer_id}

product[details][references][reference]  = {reference_code}
product[details][references][mpn]        = {mpn}
product[details][references][upc]        = {upc}
product[details][references][ean_13]     = {ean}
product[details][references][isbn]       = {isbn}
product[details][show_condition]         = 0|1

product[stock][quantities][delta_quantity][initial_quantity] = {initial_qty}
product[stock][quantities][delta_quantity][quantity]         = {current_qty}
product[stock][quantities][delta_quantity][delta]            = {delta}
product[stock][quantities][minimal_quantity]                 = {minimal_qty}
product[stock][options][stock_location]                      = {location}
product[stock][options][disabling_switch_low_stock_threshold] = 0|1
product[stock][availability][out_of_stock_type]              = 0|1|2
product[stock][availability][available_now_label][{lang_id}] = {label}
product[stock][availability][available_later_label][{lang_id}] = {label}
product[stock][availability][available_date]                 = {date}

product[shipping][dimensions][width]   = {width}
product[shipping][dimensions][height]  = {height}
product[shipping][dimensions][depth]   = {depth}
product[shipping][dimensions][weight]  = {weight}
product[shipping][delivery_time_note_type]    = {type}
product[shipping][additional_shipping_cost]   = {cost}

product[pricing][retail_price][price_tax_excluded]  = {price}
product[pricing][retail_price][tax_rules_group_id]  = {tax_id}
product[pricing][retail_price][price_tax_included]  = {price_incl_tax}
product[pricing][wholesale_price]                   = {wholesale_price}
product[pricing][disabling_switch_unit_price]       = 0|1
product[pricing][priority_management][use_custom_priority] = 0|1

product[seo][meta_title][{lang_id}]      = {meta_title}
product[seo][meta_description][{lang_id}] = {meta_description}
product[seo][link_rewrite][{lang_id}]    = {url_slug}
product[seo][redirect_option][type]      = default|temporary|permanent
product[seo][tags][{lang_id}]            = {tags}

product[options][visibility][visibility]           = both|catalog|search|none
product[options][visibility][available_for_order]  = 0|1
product[options][visibility][online_only]          = 0|1

product[footer][save]  = {value}
product[_token]        = {csrf_token}
```

## Example: Create a Simple Product

```
POST /admin_hackathon/index.php/sell/catalog/products-v2/30/edit

product[header][name][1]=Test Product
product[header][type]=standard
product[header][active]=1

product[description][description_short][1]=<p>Short description</p>
product[description][categories][product_categories][0][id]=2
product[description][categories][product_categories][0][name]=Home
product[description][categories][product_categories][0][display_name]=Home
product[description][categories][default_category_id]=2

product[details][references][reference]=TEST-001

product[stock][quantities][delta_quantity][initial_quantity]=100
product[stock][quantities][delta_quantity][quantity]=100
product[stock][quantities][minimal_quantity]=1

product[pricing][retail_price][price_tax_excluded]=29.99
product[pricing][retail_price][tax_rules_group_id]=1

product[options][visibility][visibility]=both
product[options][visibility][available_for_order]=1

product[_token]={csrf_token}
```

## Key Notes

1. **Language IDs**: Arrays use language ID as index (1 = English, likely)
2. **Category Structure**: Must include `id`, `name`, and `display_name` for each category
3. **Form Arrays**: Use bracket notation for nested arrays and indexed collections
4. **CSRF Token**: Required in `product[_token]`
5. **Image URLs**: `cover_thumbnail` expects full HTTP URL
6. **Pricing**: Separate fields for tax-excluded and tax-included prices
7. **Stock Delta**: `delta_quantity` tracks changes in quantity
8. **Product Type**: Options are `standard`, `virtual`, `pack`
9. **Visibility**: Can be `both`, `catalog`, `search`, or `none`

## Implementation Strategy

Since this is a form submission (not a REST API), there are two approaches:

### Option 1: Use Selenium to fill the form (UI-based)
- Navigate to product page
- Fill form fields
- Submit
- **Pro**: Guaranteed to match admin UI behavior
- **Con**: Slower, dependent on page structure

### Option 2: Send form data directly via HTTP POST
- Build nested array structure
- URL-encode properly
- Include CSRF token from page
- **Pro**: Faster, direct API
- **Con**: Fragile if form structure changes

## CSRF Token Acquisition

The `_token` is visible in the form. To create a new product:
1. GET the form page first
2. Extract CSRF token from form
3. Build form data with token
4. POST back to same URL

