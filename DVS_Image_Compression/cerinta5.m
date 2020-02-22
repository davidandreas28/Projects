function cerinta5()
  A = imread('./in/images/image1.gif');
  
  [m n ] = size(A);
  k = [1:19 20:20:99 100:30:min(m,n)];
  [U S V] = svd(A);
  x = diag(S);
  
  for i = 1:length(k)
    sum_1 = 0;
    sum_2 = 0;
    for j = 1:k(i)
      sum_1 += x(j);
    endfor
    for j = 1: min(m,n)
      sum_2 += x(j);
    endfor
    
    info(i) = sum_1 / sum_2;
  endfor
  
  eroare = zeros(1,length(k));
  for z= 1:length(k)
    A_k = cerinta3('./in/images/image1.gif', k(z));
    for i = 1:m
      for j = 1:n
        eroare(z) += (A(i,j) - A_k(i, j))*(A(i,j) - A_k(i, j));
      endfor
    endfor
    eroare(z) = eroare(z) / (m*n);
    rata_compresie(z) = ( k(z) * 2 + 1 ) / n;
  endfor
  
  subplot(2,2,1)
    plot(x);
    title('Graficul Valorilor Singulare');
  subplot(2,2,2)
    plot(k, info);
    title('Graficul Informatiei');
  subplot(2,2,3)
    plot(k, eroare);
    title('Graficul erorii');
  subplot(2,2,4)
    plot(k, rata_compresie)
    title('Graficul ratei de compresie');
endfunction