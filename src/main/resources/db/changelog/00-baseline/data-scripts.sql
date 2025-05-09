-- Column: public.items.price

-- ALTER TABLE IF EXISTS public.items DROP COLUMN IF EXISTS price;

ALTER TABLE IF EXISTS public.items ADD COLUMN price numeric;
INSERT INTO public.items(description, id_cloud, quantity, flag_showcase, price)  VALUES ( 'Модель парусника', 'yZlDhbzA#uz4mA25x_YONOUdb4-Mc1Mf_ltBTzN-rYAWtt-D4kSU', '50', true, 35000.250);