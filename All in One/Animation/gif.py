import os
from PIL import Image, ImageDraw, ImageFont

# numbering is done at top right corner
def create_gif(folder_name, duration=500):
    # temporary list to store png before compile
    images = []

    folder_path = os.path.join(os.getcwd(), folder_name)

    files = [f for f in os.listdir(folder_path) if f.startswith('frame_') and f.endswith('.bmp')]

    # Sort the files by their frame number
    files.sort(key=lambda x: int(x.split('_')[1].split('.')[0]))


    for i, file_name in enumerate(files, start=1):
        if file_name.endswith(".bmp") and file_name.startswith("frame"):
            file_path = os.path.join(folder_path, file_name)

            # Open the image and refine the definition
            image = Image.open(file_path)
            image = image.resize((1000, 1000), Image.NEAREST)

            # Use Draw to render character onto images
            draw = ImageDraw.Draw(image)
            font = ImageFont.truetype("arial.ttf", 24)  # font and size
            text = f"Frame {i}"

            # obtain the text info
            text_width, text_height = draw.textsize(text, font=font)

            # put the text at top right corner
            x = image.width - text_width - 10  # Adjust the position as needed
            y = 10  # Adjust the position as needed
            draw.text((x, y), text, font=font, fill="white")

            # Append the modified image to the list
            images.append(image)

    # Save images as a GIF
    if images:
        # create a new folder as given line below, and render the gif into the folder.
        output_filename = "Expanding_100"
        output_path = os.path.join(os.getcwd(), f"{output_filename}.gif")
        images[0].save(output_path, save_all=True, append_images=images[1:], duration=duration, loop=0)
        print(f"GIF created successfully at {output_path}")
    else:
        print("No BMP images found in the folder.")


# numbering is done at button right corner (with extra black strip)
def create_gif_text_at_buttom(folder_name, duration=50):
    images = []

    folder_path = os.path.join(os.getcwd(), folder_name)

    valid_extensions = ['.bmp', '.png']  # List of valid file extensions
    files = [f for f in os.listdir(folder_path) if
             f.startswith('frame_') and any(f.endswith(ext) for ext in valid_extensions)]
    # replace the below code
    # files = [f for f in os.listdir(folder_path) if f.startswith('frame_') and f.endswith('.bmp' or '.png')]

    # Sort the files by their frame number
    files.sort(key=lambda x: int(x.split('_')[1].split('.')[0]))


    for i, file_name in enumerate(files, start=1):
        if any(file_name.endswith(ext) for ext in valid_extensions) and file_name.startswith("frame"):
            file_path = os.path.join(folder_path, file_name)

            print(file_path)
            # Open the image
            image = Image.open(file_path)
            image = image.resize((1000, 1000), Image.NEAREST)

            new_img = Image.new('RGBA', (1000, 1050), (255,255,255,0))
            new_img.paste(image, (0, 0))

            # Add frame number text to the image
            draw = ImageDraw.Draw(new_img)
            font = ImageFont.truetype("arial.ttf", 24)  # Change the font and size as needed
            text = f"Frame {i}"
            text_width, text_height = draw.textsize(text, font=font)
            x = image.width - text_width - 10  # Adjust the position as needed
            y = 1000  # Adjust the position as needed
            draw.text((x, y), text, font=font, fill="black")

            # Append the modified image to the list
            images.append(new_img)

    # Save images as a GIF
    if images:
        output_filename =  folder_name + "_buttom_text"
        output_path = os.path.join(os.getcwd(), f"{output_filename}.gif")
        images[0].save(output_path, save_all=True, append_images=images[1:], duration=duration, loop=0)
        print(f"GIF created successfully at {output_path}")
    else:
        print("No BMP images found in the folder.")


# Provide folder name
folder_name = ["Expanding_100", "Expanding_512", "movingZ_100", "MovingZ_256"]

# Set the output filename
output_filename = folder_name

# Create the GIF with frame numbers
create_gif_text_at_buttom(folder_name[3])

# duration for each frame created is by default 0.5 second