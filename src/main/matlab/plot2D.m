function plot2D( input, name )
%PLOT Summary of this function goes here
%   Detailed explanation goes here



x = unique(input(:, 1));
y = unique(input(:, 2));
t = unique(input(:, 3));

rangeZ = [min(input(:, 4)) max(input(:, 4))];

img_number = 0;

for i = t'
    plotTimestep(input, i, img_number, name);
    img_number = img_number + 1;
end

for i = t(length(t):-1:1)'
    plotTimestep(input, i, img_number, name);
    img_number = img_number + 1;
end

function plotTimestep(input, i,img_number, name) 
    inputForT = input(input(:, 3) == i, :);
        
    z =  reshape(inputForT(:, 4), length(x), length(y));

    surf(x, y, z);
    zlim(rangeZ);
    print('-djpeg', '-r150', ['fig_' name '_' num2str(img_number, '%05d') '.jpg']);
end

end
