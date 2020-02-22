function A_k = cerinta1(image, k)
   A = imread(image);
   [U S V] = svd(A);
   V = V';
   U_f = U(:, 1:k);
   S_f = S(1:k, 1:k);
   V_f = V(1:k, :);
   A_k = U_f * S_f * V_f;
endfunction