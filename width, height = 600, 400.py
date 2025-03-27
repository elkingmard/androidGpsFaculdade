width, height = 600, 400
screen = pygame.display.set_mode((width, height))
clock = pygame.time.Clock()

snake_pos = [[100, 50], [90, 50], [80, 50]]
snake_direction = 'RIGHT'
food_pos = [random.randrange(1, (width//10)) * 10, random.randrange(1, (height//10)) * 10]
food_spawn = True

def game_loop():
    global food_spawn
    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                return
            
            # تحكم في الاتجاه
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_UP:
                    snake_direction = 'UP'
                elif event.key == pygame.K_DOWN:
                    snake_direction = 'DOWN'
                elif event.key == pygame.K_LEFT:
                    snake_direction = 'LEFT'
                elif event.key == pygame.K_RIGHT:
                    snake_direction = 'RIGHT'
        
        # تحديث موقع الدودة
        # ... (ملئ الجزء هنا)

        pygame.display.flip()
        clock.tick(20)

game_loop()
