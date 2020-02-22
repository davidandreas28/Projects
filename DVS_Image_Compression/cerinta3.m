function [Ak S] = cerinta3(image, k)
    A = imread(image);
    A = double(A);

    [m n] = size(A);

    for i = 1 : m
        sum = 0;
        for j = 1 : n
          sum += A(i, j);
        endfor
        M(i) = sum / n;
    end

    for i = 1 : m
        A(i, :) = A(i, :) - M(i);
    end

    Z = A' / sqrt(n - 1);

    [U S V] = svd(Z);

    W = V( : , 1 : k);

    Y = W' * A;
    Ak = W * Y + M';
end
